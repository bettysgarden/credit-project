package ru.test.conveyor.service;

import com.example.credit.application.model.CreditDTO;
import com.example.credit.application.model.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.test.conveyor.entity.Credit;
import ru.test.conveyor.entity.Employment;
import ru.test.conveyor.entity.PaymentScheduleElement;
import ru.test.conveyor.entity.ScoringData;
import ru.test.conveyor.enums.Gender;
import ru.test.conveyor.enums.MaritalStatus;
import ru.test.conveyor.exception.CreditCalculationException;
import ru.test.conveyor.exception.CreditDeclinedException;
import ru.test.conveyor.exception.InvalidScoringDataException;
import ru.test.conveyor.mapper.CreditMapper;
import ru.test.conveyor.mapper.ScoringDataMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {
    private static final Logger logger = LoggerFactory.getLogger(ScoringServiceImpl.class);
    private static final BigDecimal BASE_RATE = new BigDecimal("25");

    private final ScoringDataMapper scoringDataMapper;
    private final CreditMapper creditMapper;

    @Override
    public CreditDTO getCreditCalculation(ScoringDataDTO scoringDataDTO) {
        logger.info("Получен запрос на расчёт кредита для данных: {}", scoringDataDTO);

        ScoringData scoringData = scoringDataMapper.toEntity(scoringDataDTO);

        return scoring(scoringData);
    }

    public CreditDTO scoring(ScoringData scoringData) {
        BigDecimal rate = BASE_RATE;

        validateScoringData(scoringData);

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
            logger.info("Итоговый расчёт кредита: {}", credit);

            return creditMapper.toDto(credit);
        } catch (InvalidScoringDataException ex) {
            logger.error("Ошибка валидации скоринговых данных: {}", ex.getMessage());
            throw ex;
        } catch (CreditDeclinedException ex) {
            logger.error("Ошибка расчёта кредита: ", ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Ошибка расчёта кредита: ", ex);
            throw new CreditCalculationException("Внутренняя ошибка расчёта кредита. Попробуйте позже.", ex);
        }
    }

    private void validateScoringData(ScoringData scoringData) {
        if (scoringData.getBirthdate() == null) {
            throw new InvalidScoringDataException("Дата рождения не указана.");
        }

        int age = Period.between(scoringData.getBirthdate(), LocalDate.now()).getYears();
        if (age < 20 || age > 60) {
            throw new CreditDeclinedException("Клиент не прошёл по возрасту.");
        }

        Employment employment = scoringData.getEmployment();
        if (employment == null) {
            throw new InvalidScoringDataException("Данные о занятости не указаны.");
        }

        if (employment.getWorkExperienceTotal() == null || employment.getWorkExperienceCurrent() == null) {
            throw new InvalidScoringDataException("Недостаточные данные о стаже работы.");
        }

        if (employment.getWorkExperienceTotal() < 12 || employment.getWorkExperienceCurrent() < 3) {
            throw new CreditDeclinedException("Недостаточный стаж работы: общий стаж должен быть минимум 12 месяцев, а текущий — минимум 3 месяца.");
        }

        if (scoringData.getAmount() == null || employment.getSalary() == null) {
            throw new InvalidScoringDataException("Не указаны сумма кредита или зарплата.");
        }

        if (scoringData.getAmount().compareTo(employment.getSalary().multiply(BigDecimal.valueOf(20))) > 0) {
            throw new CreditDeclinedException("Сумма кредита превышает допустимое соотношение 20-кратного размера зарплаты.");
        }

        if (scoringData.getTerm() == null || scoringData.getTerm() < 6 || scoringData.getTerm() > 60) {
            throw new InvalidScoringDataException("Неверно указан срок кредита. Допустимый срок — от 6 до 60 месяцев.");
        }

        if (scoringData.getDependentAmount() != null && scoringData.getDependentAmount() < 0) {
            throw new InvalidScoringDataException("Некорректное количество иждивенцев: оно не может быть отрицательным.");
        }

        if (scoringData.getIsInsuranceEnabled() == null) {
            throw new InvalidScoringDataException("Не указано, оформлена ли страховка.");
        }

        if (scoringData.getIsSalaryClient() == null) {
            throw new InvalidScoringDataException("Не указано, является ли клиент зарплатным клиентом.");
        }
    }

    private BigDecimal adjustRateForEmployment(Employment employment, BigDecimal rate) {
        switch (employment.getEmploymentStatus()) {
            case UNEMPLOYED:
                logger.warn("Клиент безработный, отклонение заявки");
                throw new CreditDeclinedException("Клиенту отказано в кредите на основании скоринговых проверок.");
            case SELF_EMPLOYED:
                rate = rate.add(BigDecimal.ONE);
                logger.info("Ставка увеличена на 1% для самозанятого клиента");
                break;
            case BUSINESS_OWNER:
                rate = rate.add(BigDecimal.valueOf(3));
                logger.info("Ставка увеличена на 3% для владельца бизнеса");
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
                logger.info("Ставка уменьшена на 3% для женатого клиента");
                break;
            case DIVORCED:
                rate = rate.add(BigDecimal.ONE);
                logger.info("Ставка увеличена на 1% для разведённого клиента");
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
                    logger.info("Ставка уменьшена на 3% для женщины в возрасте 35-60 лет");
                }
                break;
            case MALE:
                if (age >= 30 && age <= 55) {
                    rate = rate.subtract(BigDecimal.valueOf(3));
                    logger.info("Ставка уменьшена на 3% для мужчины в возрасте 30-55 лет");
                }
                break;
            case NON_BINARY:
                rate = rate.add(BigDecimal.valueOf(3));
                logger.info("Ставка увеличена на 3% для клиента с полом 'не бинарный'");
                break;
            default:
                break;
        }
        return rate;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        return amount.
                multiply(monthlyRate).divide(
                        BigDecimal.ONE.subtract(
                                BigDecimal.ONE.divide(
                                        (BigDecimal.ONE.add(monthlyRate)).pow(term), 6, RoundingMode.HALF_UP)),
                        2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePsk(BigDecimal monthlyPayment, Integer term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }

    private ArrayList<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal monthlyPayment, Integer term, BigDecimal amount, BigDecimal rate) {
        ArrayList<PaymentScheduleElement> paymentSchedule = new ArrayList<>();
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);

        for (int i = 1; i <= term; i++) {
            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement();
            paymentScheduleElement.setNumber(i);
            paymentScheduleElement.setDate(LocalDate.now().plusMonths(i));
            paymentScheduleElement.setTotalPayment(monthlyPayment);

            BigDecimal interest = amount.multiply(monthlyRate).divide(BigDecimal.valueOf(1), 2, RoundingMode.HALF_UP);
            paymentScheduleElement.setInterestPayment(interest);

            BigDecimal monthlyDebtPayment = monthlyPayment.subtract(interest);
            paymentScheduleElement.setDebtPayment(monthlyDebtPayment);

            amount = amount.subtract(monthlyDebtPayment);
            paymentScheduleElement.setRemainingDebt(amount);

            paymentSchedule.add(paymentScheduleElement);
        }

        return paymentSchedule;
    }
}
