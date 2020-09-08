package com.mkcode.factoring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceDto {
    @NotBlank
    private String contractNo;
    @NotNull
    private LocalDate issueDate;
    @NotNull
    private LocalDate dueDate;
    private LocalDate paidDate;
    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    @Positive
    private BigDecimal amount;
}