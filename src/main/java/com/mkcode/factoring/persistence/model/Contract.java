package com.mkcode.factoring.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ContractId.class)
public class Contract {
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

@Data
@Embeddable
class ContractId implements Serializable {
    private String contractNo;
    private LocalDate startDate;
}