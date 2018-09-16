package com.marinho.bankslips.api;

import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.service.BankSlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
public class Api {

    @Autowired
    private BankSlipService service;

    @GetMapping
    public Collection<BankSlipResponse> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody final BankSlipRequest request) {

        final BankSlipResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public Resource<BankSlipResponse> get(@PathVariable final String id) {
        final BankSlipResponse response = service.findById(id);

        return new Resource<>(response);
    }

    @PutMapping("/{id}/payments")
    public ResponseEntity pay(@PathVariable final String id) {
        service.pay(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity cancel(@PathVariable final String id) {
        service.cancel(id);

        return ResponseEntity.noContent().build();
    }
}
