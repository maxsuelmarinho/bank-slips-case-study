package com.marinho.bankslips.mapper;

import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.model.BankSlip;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class BankSlipEntityToBankSlipResponseConverter extends AbstractConverter<BankSlip, BankSlipResponse> {

    @Override
    public BankSlipResponse convert(BankSlip source) {

        return BankSlipResponse.builder()
                .customer(source.getCustomer())
                .dueDate(source.getDueDate())
                .id(source.getUuid())
                .totalInCents(source.getTotalInCents())
                .status(source.getStatus() != null ? source.getStatus().name() : null)
                .build();
    }
}
