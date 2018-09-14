package com.marinho.bankslips.service;

import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class BankSlipService {

    private List<BankSlipResponse> bankSlips;

    BankSlipService() {
        bankSlips = Arrays.asList(
                BankSlipResponse.builder()
                        .id("asdasdasd")
                        .dueDate("2018-09-12")
                        .totalInCents("1000000000")
                        .customer("Fake Company")
                        .status("PENDING")
                        .build(),
                BankSlipResponse.builder()
                        .id("qweqweqwe")
                        .dueDate("2018-09-13")
                        .totalInCents("1000000001")
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
                .get();
    }

    public void pay(final String id) {
        final BankSlipResponse bankSlip = findById(id);

        bankSlip.setStatus("PAID");
    }
}
