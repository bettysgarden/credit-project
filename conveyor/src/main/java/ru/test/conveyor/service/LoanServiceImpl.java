package ru.test.conveyor.service;

import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.test.conveyor.entity.LoanApplication;
import ru.test.conveyor.entity.LoanOffer;
import ru.test.conveyor.exception.InvalidLoanApplicationException;
import ru.test.conveyor.exception.LoanCalculationException;
import ru.test.conveyor.mapper.LoanApplicationMapper;
import ru.test.conveyor.mapper.LoanOfferMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);
    private static final BigDecimal BASE_RATE = new BigDecimal("25");

    private final LoanOfferMapper loanOfferMapper;
    private final LoanApplicationMapper loanApplicationMapper;

    @Override
    public List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationDTO) {
        logger.info("Получен запрос на получение кредитных предложений для заявки: {}", loanApplicationDTO);

        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationDTO);

        if (!prescoring(loanApplication)) {
            logger.warn("Предварительная проверка не пройдена для заявки: {}", loanApplication);
            throw new InvalidLoanApplicationException("Предварительная проверка не пройдена. Проверьте введенные данные.");
        }


        logger.info("Предварительная проверка пройдена успешно, формируем кредитные предложения");

        try {
            List<LoanOfferDTO> offers = new ArrayList<>();
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, true, true)));
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, true, false)));
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, false, true)));
            offers.add(loanOfferMapper.toDTO(getLoanOffer(loanApplication, false, false)));

            offers.sort(Comparator.comparing(LoanOfferDTO::getRate));
            logger.info("Сформировано {} кредитных предложений для заявки: {}", offers.size(), loanApplication);
            return offers;
        } catch (Exception e) {
            logger.error("Ошибка при формировании кредитных предложений: ", e);
            throw new LoanCalculationException("Ошибка при расчете предложений.");
        }
    }

    private LoanOffer getLoanOffer(LoanApplication application, Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        try {
            LoanOffer loanOffer = new LoanOffer();
            BigDecimal amount = application.getAmount();
            BigDecimal rate = BASE_RATE;

            logger.info("Начало расчета кредитного предложения для страховки: {}, зарплатный клиент: {}", isInsuranceEnabled, isSalaryClient);

            if (isInsuranceEnabled && isSalaryClient) {
                rate = rate.subtract(BigDecimal.valueOf(6));
                amount = amount.add(calculateInsuranceCost(application));
                logger.info("Ставка снижена на 6%, сумма увеличена на стоимость страховки");
            } else if (isInsuranceEnabled) {
                rate = rate.subtract(BigDecimal.valueOf(5));
                amount = amount.add(calculateInsuranceCost(application));
                logger.info("Ставка снижена на 5%, сумма увеличена на стоимость страховки");
            } else if (isSalaryClient) {
                rate = rate.subtract(BigDecimal.valueOf(1));
                logger.info("Ставка снижена на 1% за участие в программе зарплатного клиента");
            }

            BigDecimal monthlyPayment = calculateMonthlyPayment(amount, rate, application.getTerm());
            logger.info("Расчет аннуитетного платежа завершен, ежемесячный платеж: {}", monthlyPayment);

            loanOffer.setApplicationId(null); // TODO как формируется applicationId
            loanOffer.setTotalAmount(amount);
            loanOffer.setTerm(application.getTerm());
            loanOffer.setRate(rate);
            loanOffer.setMonthlyPayment(monthlyPayment);
            loanOffer.setIsInsuranceEnabled(isInsuranceEnabled);
            loanOffer.setIsSalaryClient(isSalaryClient);
            loanOffer.setRequestedAmount(application.getAmount());

            logger.info("Кредитное предложение сформировано: {}", loanOffer);
            return loanOffer;
        } catch (Exception e) {
            logger.error("Ошибка при расчете кредитного предложения: ", e);
            throw new LoanCalculationException("Ошибка расчета кредитного предложения.");
        }
    }


    private BigDecimal calculateInsuranceCost(LoanApplication application) {
        // 10000 + (запрашиваемая_сумма/1000) * (количество_платежных_периодов)
        BigDecimal insuranceBase = new BigDecimal("10000");
        BigDecimal insuranceVariable = application.getAmount().divide(BigDecimal.valueOf(1000), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(application.getTerm()));
        BigDecimal totalInsuranceCost = insuranceBase.add(insuranceVariable);
        logger.info("Расчет стоимости страховки: базовая часть {}, переменная часть {}, итоговая сумма {}", insuranceBase, insuranceVariable, totalInsuranceCost);
        return totalInsuranceCost;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = amount.
                multiply(monthlyRate).divide(
                        BigDecimal.ONE.subtract(
                                BigDecimal.ONE.divide(
                                        (BigDecimal.ONE.add(monthlyRate)).pow(term),6, RoundingMode.HALF_UP)),
                        2, RoundingMode.HALF_UP);
        logger.info("Расчет аннуитетного платежа: сумма {}, ставка {}, срок {}, ежемесячный платеж {}", amount, rate, term, monthlyPayment);
        return monthlyPayment;
    }

    public boolean prescoring(LoanApplication loanApplication) {
        logger.info("Начало предварительной проверки заявки: {}", loanApplication);

        if (isValidName(loanApplication.getFirstName()) || isValidName(loanApplication.getLastName())) {
            logger.warn("Некорректное имя или фамилия: {} {}", loanApplication.getFirstName(), loanApplication.getLastName());
            return false;
        }

        if (loanApplication.getMiddleName() != null && isValidName(loanApplication.getMiddleName())) {
            logger.warn("Некорректное отчество: {}", loanApplication.getMiddleName());
            return false;
        }

        if (loanApplication.getAmount().compareTo(BigDecimal.valueOf(10000)) < 0) {
            logger.warn("Сумма кредита меньше минимально допустимой: {}", loanApplication.getAmount());
            return false;
        }

        if (loanApplication.getTerm() == null || loanApplication.getTerm() < 6) {
            logger.warn("Некорректный срок кредита: {}", loanApplication.getTerm());
            return false;
        }

        if (!isValidAge(loanApplication.getBirthdate())) {
            logger.warn("Клиент не прошел по возрасту: {}", loanApplication.getBirthdate());
            return false;
        }

        if (!isValidEmail(loanApplication.getEmail())) {
            logger.warn("Некорректный email: {}", loanApplication.getEmail());
            return false;
        }

        if (!isValidPassportSeries(loanApplication.getPassportSeries()) || !isValidPassportNumber(loanApplication.getPassportNumber())) {
            logger.warn("Некорректные данные паспорта: серия {}, номер {}", loanApplication.getPassportSeries(), loanApplication.getPassportNumber());
            return false;
        }

        logger.info("Предварительная проверка пройдена успешно для заявки: {}", loanApplication);
        return true;
    }



    private boolean isValidName(String name) {
        return name == null || !name.matches("[A-Za-z]{2,30}");
    }

    private boolean isValidAge(LocalDate birthdate) {
        if (birthdate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears() >= 18;
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[\\w.]{2,50}@[\\w.]{2,20}";
        return email != null && Pattern.matches(emailPattern, email);
    }

    private boolean isValidPassportSeries(String passportSeries) {
        return passportSeries != null && passportSeries.matches("\\d{4}");
    }

    private boolean isValidPassportNumber(String passportNumber) {
        return passportNumber != null && passportNumber.matches("\\d{6}");
    }
}
