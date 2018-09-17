package com.marinho.bankslips.configuration;

import com.marinho.bankslips.dto.BankSlipDetailsResponse;
import com.marinho.bankslips.dto.BankSlipResponse;
import com.marinho.bankslips.model.BankSlip;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankSlipConfiguration {

    @Autowired
    private Converter<BankSlip, BankSlipResponse> bankSlipEntityToBankSlipResponseConverter;

    @Autowired
    private Converter<BankSlip, BankSlipDetailsResponse> bankSlipEntityToBankSlipDetailsResponseConverter;

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(bankSlipEntityToBankSlipResponseConverter);
        modelMapper.addConverter(bankSlipEntityToBankSlipDetailsResponseConverter);
        return modelMapper;
    }

}
