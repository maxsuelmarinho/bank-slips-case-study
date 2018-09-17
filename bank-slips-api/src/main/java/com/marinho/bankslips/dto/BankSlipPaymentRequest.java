package com.marinho.bankslips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankSlipPaymentRequest implements Serializable {

    @JsonProperty("payment_date")
    @NotNull
    private LocalDate paymentDate;
}
