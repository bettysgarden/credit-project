package ru.test.conveyor.service;


import com.example.credit.application.model.LoanApplicationRequestDTO;
import com.example.credit.application.model.LoanOfferDTO;
import ru.test.conveyor.model.entity.LoanApplication;
import ru.test.conveyor.model.entity.LoanOffer;

import java.util.List;

public interface LoanService {
    List<LoanOfferDTO> getLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO);

    LoanOffer getLoanOffer(LoanApplication application, Boolean isInsuranceEnabled, Boolean isSalaryClient);
}
