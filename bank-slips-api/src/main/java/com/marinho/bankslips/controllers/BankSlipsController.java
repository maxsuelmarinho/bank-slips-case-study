package com.marinho.bankslips.controllers;

import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
public class BankSlipsController {

    private final List<BankSlipResponse> bankSlips = Arrays.asList(
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

    @GetMapping
    public Collection<BankSlipResponse> list() {
        return bankSlips;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody final BankSlipRequest request) {

        final BankSlipResponse response = BankSlipResponse.builder()
                .id(UUID.randomUUID().toString())
                .dueDate(request.getDueDate())
                .totalInCents(request.getTotalInCents())
                .customer(request.getCustomer())
                .status("PENDING")
                .build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }
}
