package com.mkcode.factoring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractDto {
    @Id
    @NotBlank
    private String contractNo;
    private boolean active;
    @Id
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull
    @PositiveOrZero
    private BigDecimal fixedFee;
    @NotNull
    @PositiveOrZero
    private int daysIncluded;
    @NotNull
    @PositiveOrZero
    private BigDecimal additionalFee;
}
