package com.marinho.bankslips.service;


import com.marinho.bankslips.dto.BankSlipRequest;
import com.marinho.bankslips.dto.BankSlipResponse;

import java.util.Collection;

public interface IBankSlipService {

    Collection<BankSlipResponse> findAll();

    BankSlipResponse create(BankSlipRequest request);

    BankSlipResponse findById(final String id);

    void pay(final String id);

    void cancel(String id);
}
