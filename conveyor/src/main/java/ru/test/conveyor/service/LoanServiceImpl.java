package ru.test.conveyor.service;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.test.conveyor.entity.LoanApplication;
import ru.test.conveyor.entity.LoanOffer;
import ru.test.conveyor.exception.InvalidLoanApplicationException;
import ru.test.conveyor.exception.LoanCalculationException;
import ru.test.conveyor.mapper.LoanApplicationMapper;
import ru.test.conveyor.mapper.LoanOfferMapper;
import ru.test.conveyor.util.LoanApplicationValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ru.test.conveyor.util.CreditCalculator.calculateInsuranceCost;
import static ru.test.conveyor.util.CreditCalculator.calculateMonthlyPayment;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private static final BigDecimal BASE_RATE = new BigDecimal("25");

    private final LoanOfferMapper loanOfferMapper;
    private final LoanApplicationMapper loanApplicationMapper;
    private final LoanApplicationValidator validator = new LoanApplicationValidator();

    public static Long generateApplicationId() {
        long timestamp = System.currentTimeMillis();
        long randomPart = ThreadLocalRandom.current().nextLong(10000, 99999);
        return timestamp * (-100000) + randomPart;
    }

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationDTO) {
        log.info("Получен запрос на получение кредитных предложений для заявки: {}", loanApplicationDTO);

        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationDTO);

        List<String> validationErrors = validator.validate(loanApplication);

        if (!validationErrors.isEmpty()) {
            log.warn("Заявка не прошла проверку: {}", validationErrors);
            throw new InvalidLoanApplicationException("Предварительная проверка не пройдена.", validationErrors);
        }

        log.info("Предварительная проверка пройдена успешно, формируем кредитные предложения");


        try {
            List<LoanOfferDTO> offers = new ArrayList<>();
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, true, true)));
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, true, false)));
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, false, true)));
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, false, false)));

            offers.sort(Comparator.comparing(LoanOfferDTO::getRate));
            log.info("Сформировано {} кредитных предложений для заявки: {}", offers.size(), loanApplication);
            return offers;
        } catch (Exception e) {
            log.error("Ошибка при формировании кредитных предложений: ", e);
            throw new LoanCalculationException("Ошибка при расчете предложений.");
        }
    }

    private LoanOffer getLoanOffer(LoanApplication application, Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        try {
            LoanOffer loanOffer = new LoanOffer();
            BigDecimal amount = application.getAmount();
            BigDecimal rate = BASE_RATE;

            log.info("Начало расчета кредитного предложения для страховки: {}, зарплатный клиент: {}", isInsuranceEnabled, isSalaryClient);

            if (isInsuranceEnabled && isSalaryClient) {
                rate = rate.subtract(BigDecimal.valueOf(6));
                amount = amount.add(calculateInsuranceCost(application));
                log.info("Ставка снижена на 6%, сумма увеличена на стоимость страховки");
            } else if (isInsuranceEnabled) {
                rate = rate.subtract(BigDecimal.valueOf(5));
                amount = amount.add(calculateInsuranceCost(application));
                log.info("Ставка снижена на 5%, сумма увеличена на стоимость страховки");
            } else if (isSalaryClient) {
                rate = rate.subtract(BigDecimal.valueOf(1));
                log.info("Ставка снижена на 1% за участие в программе зарплатного клиента");
            }

            BigDecimal monthlyPayment = calculateMonthlyPayment(amount, rate, application.getTerm());
            log.info("Расчет аннуитетного платежа завершен, ежемесячный платеж: {}", monthlyPayment);

            loanOffer.setApplicationId(generateApplicationId());
            loanOffer.setTotalAmount(amount);
            loanOffer.setTerm(application.getTerm());
            loanOffer.setRate(rate);
            loanOffer.setMonthlyPayment(monthlyPayment);
            loanOffer.setIsInsuranceEnabled(isInsuranceEnabled);
            loanOffer.setIsSalaryClient(isSalaryClient);
            loanOffer.setRequestedAmount(application.getAmount());

            log.info("Кредитное предложение сформировано: {}", loanOffer);
            return loanOffer;
        } catch (Exception e) {
            log.error("Ошибка при расчете кредитного предложения: ", e);
            throw new LoanCalculationException("Ошибка расчета кредитного предложения.");
        }
    }
}
