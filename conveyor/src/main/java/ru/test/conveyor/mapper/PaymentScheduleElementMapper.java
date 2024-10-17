package ru.test.conveyor.mapper;

import com.example.credit.application.model.PaymentScheduleElementDTO;
import org.mapstruct.Mapper;
import ru.test.conveyor.entity.PaymentScheduleElement;

@Mapper
public interface PaymentScheduleElementMapper {
    PaymentScheduleElement toEntity(PaymentScheduleElementDTO dto);

    PaymentScheduleElementDTO toDto(PaymentScheduleElement entity);
}
