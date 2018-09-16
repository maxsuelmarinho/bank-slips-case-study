package com.marinho.bankslips.service;

import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.exception.BankSlipNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class BankSlipService {

    private List<BankSlipResponse> bankSlips = new ArrayList<>();

    BankSlipService() {
        bankSlips.add(BankSlipResponse.builder()
                .id("asdasdasd")
                .dueDate(LocalDate.of(2018, 9, 12))
                .totalInCents(new BigDecimal("1000000000"))
                .customer("Fake Company")
                .status("PENDING")
                .build());

        bankSlips.add(BankSlipResponse.builder()
                .id("qweqweqwe")
                .dueDate(LocalDate.of(2018, 9, 12))
                .totalInCents(new BigDecimal("1000000001"))
                .customer("Real Company")
                .status("PENDING")
                .build());
    }


    public Collection<BankSlipResponse> findAll() {
        return this.bankSlips;
    }

    public BankSlipResponse create(BankSlipRequest request) {
        final BankSlipResponse response = BankSlipResponse.builder()
                .id(UUID.randomUUID().toString())
                .dueDate(request.getDueDate())
                .totalInCents(request.getTotalInCents())
                .customer(request.getCustomer())
                .status("PENDING")
                .build();

        this.bankSlips.add(response);

        return response;
    }

    public BankSlipResponse findById(final String id) {
        return this.bankSlips.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .orElseThrow(BankSlipNotFoundException::new);
    }

    public void pay(final String id) {
        final BankSlipResponse bankSlip = findById(id);

        bankSlip.setStatus("PAID");
    }

    public void cancel(String id) {
        final BankSlipResponse bankSlip = findById(id);

        bankSlip.setStatus("CANCELED");
    }
}
