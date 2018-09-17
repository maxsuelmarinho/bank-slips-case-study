package com.marinho.bankslips.api;

import com.marinho.bankslips.dto.BankSlipPaymentRequest;
import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.facade.BankSlipFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class Api {

    @Autowired
    private BankSlipFacade facade;

    @GetMapping("/bankslips")
    public List<BankSlipResponse> list() {
        return facade.findAll();
    }

    @PostMapping("/bankslips")
    public ResponseEntity<?> create(@RequestBody @Valid final BankSlipRequest request) {

        final BankSlipResponse response = facade.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/bankslips/{id}")
    public Resource<BankSlipResponse> get(@PathVariable final String id) {
        final BankSlipResponse response = facade.get(id);

        return new Resource<>(response);
    }

    @PutMapping("/bankslips/{id}/payments")
    public ResponseEntity pay(@PathVariable final String id,
                              @Valid @RequestBody final BankSlipPaymentRequest request) {
        facade.pay(id, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bankslips/{id}")
    public ResponseEntity cancel(@PathVariable final String id) {
        facade.cancel(id);

        return ResponseEntity.noContent().build();
    }
}
