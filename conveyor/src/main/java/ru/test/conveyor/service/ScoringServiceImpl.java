package ru.test.conveyor.service;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.test.conveyor.enums.Gender;
import ru.test.conveyor.enums.MaritalStatus;
import ru.test.conveyor.exception.CreditCalculationException;
import ru.test.conveyor.exception.CreditDeclinedException;
import ru.test.conveyor.exception.InvalidScoringDataException;
import ru.test.conveyor.mapper.CreditMapper;
import ru.test.conveyor.mapper.ScoringDataMapper;
import ru.test.conveyor.model.entity.Credit;
import ru.test.conveyor.model.entity.Employment;
import ru.test.conveyor.model.entity.PaymentScheduleElement;
import ru.test.conveyor.model.entity.ScoringData;
import ru.test.conveyor.util.ScoringDataValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static ru.test.conveyor.util.CreditCalculator.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {
    private static final BigDecimal BASE_RATE = new BigDecimal("25");

    private final ScoringDataMapper scoringDataMapper;
    private final CreditMapper creditMapper;
    private final ScoringDataValidator scoringDataValidator;

    @Override
    public CreditDTO getCreditCalculation(ScoringDataDTO scoringDataDTO) {
        log.info("Получен запрос на расчёт кредита для данных: {}", scoringDataDTO);

        ScoringData scoringData = scoringDataMapper.toEntity(scoringDataDTO);
        List<String> validationErrors = scoringDataValidator.validateScoringData(scoringData);

        if (!validationErrors.isEmpty()) {
            throw new InvalidScoringDataException("Скоринг не пройден:", validationErrors);
        }

        return creditMapper.toDto(scoring(scoringData));
    }

    public Credit scoring(ScoringData scoringData) {
        BigDecimal rate = BASE_RATE;

        try {
            rate = adjustRateForEmployment(scoringData.getEmployment(), rate);
            rate = adjustRateForMaritalStatus(scoringData.getMaritalStatus(), rate);
            rate = adjustRateForGenderAndAge(scoringData.getGender(), scoringData.getBirthdate(), rate);

            if (scoringData.getDependentAmount() != null && scoringData.getDependentAmount() > 1) {
                rate = rate.add(BigDecimal.ONE);
            }

            BigDecimal monthlyPayment = calculateMonthlyPayment(scoringData.getAmount(), rate, scoringData.getTerm());
            BigDecimal psk = calculatePsk(monthlyPayment, scoringData.getTerm());
            ArrayList<PaymentScheduleElement> paymentSchedule = calculatePaymentSchedule(monthlyPayment, scoringData.getTerm(), scoringData.getAmount(), rate);


            Credit credit = new Credit(
                    scoringData.getAmount(),
                    scoringData.getTerm(),
                    monthlyPayment,
                    rate,
                    psk,
                    scoringData.getIsInsuranceEnabled(),
                    scoringData.getIsSalaryClient(),
                    paymentSchedule
            );
            log.info("Итоговый расчёт кредита: {}", credit);
            return credit;
        } catch (InvalidScoringDataException ex) {
            log.error("Ошибка валидации скоринговых данных: {}", ex.getMessage());
            throw ex;
        } catch (CreditDeclinedException ex) {
            log.error("Кредит отклонен: ", ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Ошибка расчёта кредита: ", ex);
            throw new CreditCalculationException("Внутренняя ошибка расчёта кредита. Попробуйте позже.", ex);
        }
    }

    private BigDecimal adjustRateForEmployment(Employment employment, BigDecimal rate) {
        switch (employment.getEmploymentStatus()) {
            case UNEMPLOYED:
                log.warn("Клиент безработный");
                throw new CreditDeclinedException("Заявка на кредит отклонена", new ArrayList<>(List.of("Клиент безработный, отклонение заявки")));
            case SELF_EMPLOYED:
                rate = rate.add(BigDecimal.ONE);
                log.info("Ставка увеличена на 1% для самозанятого клиента");
                break;
            case BUSINESS_OWNER:
                rate = rate.add(BigDecimal.valueOf(3));
                log.info("Ставка увеличена на 3% для владельца бизнеса");
                break;
            default:
                break;
        }
        return rate;
    }

    private BigDecimal adjustRateForMaritalStatus(MaritalStatus maritalStatus, BigDecimal rate) {
        switch (maritalStatus) {
            case MARRIED:
                rate = rate.subtract(BigDecimal.valueOf(3));
                log.info("Ставка уменьшена на 3% для женатого клиента");
                break;
            case DIVORCED:
                rate = rate.add(BigDecimal.ONE);
                log.info("Ставка увеличена на 1% для разведённого клиента");
                break;
            default:
                break;
        }
        return rate;
    }

    private BigDecimal adjustRateForGenderAndAge(Gender gender, LocalDate birthDate, BigDecimal rate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        switch (gender) {
            case FEMALE:
                if (age >= 35 && age <= 60) {
                    rate = rate.subtract(BigDecimal.valueOf(3));
                    log.info("Ставка уменьшена на 3% для женщины в возрасте 35-60 лет");
                }
                break;
            case MALE:
                if (age >= 30 && age <= 55) {
                    rate = rate.subtract(BigDecimal.valueOf(3));
                    log.info("Ставка уменьшена на 3% для мужчины в возрасте 30-55 лет");
                }
                break;
            case NON_BINARY:
                rate = rate.add(BigDecimal.valueOf(3));
                log.info("Ставка увеличена на 3% для клиента с полом 'не бинарный'");
                break;
            default:
                break;
        }
        return rate;
    }


}
