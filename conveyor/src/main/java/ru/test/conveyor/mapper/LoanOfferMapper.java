package ru.test.conveyor.mapper;

import com.example.credit.application.model.LoanOfferDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.test.conveyor.model.entity.LoanOffer;

@Mapper
public interface LoanOfferMapper {
    LoanOfferMapper INSTANCE = Mappers.getMapper(LoanOfferMapper.class);

    LoanOfferDTO toDTO(LoanOffer loanOffer);
}
