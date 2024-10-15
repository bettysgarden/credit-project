package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanOfferDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.test.conveyor.entity.LoanOffer;

@Mapper
public interface LoanOfferMapper {
    LoanOffer toEntity(LoanOfferDTO loanOfferDTO);

    LoanOfferDTO toDTO(LoanOffer loanOffer);
}
