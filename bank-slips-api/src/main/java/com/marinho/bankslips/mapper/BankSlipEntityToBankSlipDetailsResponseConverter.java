package com.marinho.bankslips.mapper;

import com.marinho.bankslips.dto.BankSlipDetailsResponse;
import com.marinho.bankslips.model.BankSlip;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class BankSlipEntityToBankSlipDetailsResponseConverter extends AbstractConverter<BankSlip, BankSlipDetailsResponse> {

    @Override
    public BankSlipDetailsResponse convert(BankSlip source) {

        return BankSlipDetailsResponse.builder()
                .customer(source.getCustomer())
                .dueDate(source.getDueDate())
                .paymentDate(source.getPaymentDate())
                .fine(source.getFine())
                .id(source.getUuid())
                .totalInCents(source.getTotalInCents())
                .status(source.getStatus() != null ? source.getStatus().name() : null)
                .build();
    }
}
