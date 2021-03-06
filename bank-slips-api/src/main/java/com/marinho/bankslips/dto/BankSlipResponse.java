package com.marinho.bankslips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankSlipResponse implements Serializable {

    private String id;

    @JsonProperty("due_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Brazil/East", locale = "pt-BR")
    private LocalDate dueDate;

    @JsonProperty("total_in_cents")
    private BigDecimal totalInCents;

    private String customer;

    private String status;
}
