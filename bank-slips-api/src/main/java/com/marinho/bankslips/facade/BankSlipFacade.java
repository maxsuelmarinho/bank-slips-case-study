package com.marinho.bankslips.facade;

import com.marinho.bankslips.dto.BankSlipPaymentRequest;
import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.model.BankSlip;
import com.marinho.bankslips.service.IBankSlipService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BankSlipFacade {

    @Autowired
    private IBankSlipService service;

    @Autowired
    private ModelMapper modelMapper;

    public BankSlipResponse create(final BankSlipRequest request) {
        final BankSlip bankSlip = service.create(request.getCustomer(), request.getDueDate(), request.getTotalInCents());
        final BankSlipResponse response = convertToDto(bankSlip);
        return response;
    }

    public List<BankSlipResponse> findAll() {
        return convertToDto(service.findAll());
    }

    public BankSlipResponse get(final String id) {
        final BankSlip bankSlip = service.findByUuid(id);
        return convertToDto(bankSlip);
    }

    public void pay(final String id, final BankSlipPaymentRequest request) {
        service.pay(id, request.getPaymentDate());
    }

    public void cancel(String id) {
        service.cancel(id);
    }

    private BankSlipResponse convertToDto(final BankSlip entity) {
        return modelMapper.map(entity, BankSlipResponse.class);
    }

    private List<BankSlipResponse> convertToDto(List<BankSlip> entities) {
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
