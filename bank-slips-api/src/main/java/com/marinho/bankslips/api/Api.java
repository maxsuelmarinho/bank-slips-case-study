package com.marinho.bankslips.api;

import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.model.BankSlip;
import com.marinho.bankslips.service.IBankSlipService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController("bankslips")
public class Api {

    @Autowired
    private IBankSlipService service;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<BankSlipResponse> list() {
        return convertToDto(service.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid final BankSlipRequest request) {

        final BankSlip bankSlip = service.create(request.getCustomer(), request.getDueDate(), request.getTotalInCents());
        final BankSlipResponse response = convertToDto(bankSlip);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public Resource<BankSlipResponse> get(@PathVariable final String id) {
        final BankSlip bankSlip = service.findByUuid(id);
        final BankSlipResponse response = convertToDto(bankSlip);

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

    private BankSlipResponse convertToDto(final BankSlip entity) {
        return modelMapper.map(entity, BankSlipResponse.class);
    }

    private List<BankSlipResponse> convertToDto(List<BankSlip> entities) {
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
