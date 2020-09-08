package com.mkcode.factoring.service;

import com.mkcode.factoring.model.InvoiceDto;
import com.mkcode.factoring.persistence.ContractRepository;
import com.mkcode.factoring.persistence.model.Contract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static java.lang.Math.max;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@RequiredArgsConstructor
@Service
public class FeeService {
    private final ContractRepository repository;

    public BigDecimal calculate(InvoiceDto invoiceDto) {
        var contract = repository.findActualContract(invoiceDto.getContractNo(), invoiceDto.getIssueDate())
                .orElseThrow(() -> new ContractNotFoundException(invoiceDto.getContractNo(), invoiceDto.getIssueDate()));

        return calculateFixedFee(contract, invoiceDto).add(calculateAdditionalFee(contract, invoiceDto))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAdditionalFee(Contract contract, InvoiceDto invoiceDto) {
        var additionalDays = max(0, DAYS.between(invoiceDto.getPurchaseDate().plusDays(contract.getDaysIncluded() - 1),
                defaultIfNull(invoiceDto.getPaidDate(), LocalDate.now())));
        return invoiceDto.getAmount()
                .multiply(contract.getAdditionalFee().divide(BigDecimal.valueOf(100)))
                .multiply(BigDecimal.valueOf(additionalDays));
    }

    private BigDecimal calculateFixedFee(Contract contract, InvoiceDto invoiceDto) {
        return invoiceDto.getAmount()
                .multiply(contract.getFixedFee().divide(BigDecimal.valueOf(100)));
    }
}
