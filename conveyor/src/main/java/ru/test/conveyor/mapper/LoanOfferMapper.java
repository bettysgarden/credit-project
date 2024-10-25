package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanOfferDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.model.entity.LoanOffer;

@Mapper
public interface LoanOfferMapper {

    LoanOfferDTO toDTO(LoanOffer loanOffer);
}
