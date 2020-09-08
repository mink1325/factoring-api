package com.mkcode.factoring.rest;

import com.mkcode.factoring.model.InvoiceDto;
import com.mkcode.factoring.service.FeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping(path = "/fee")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    @ResponseStatus(OK)
    @PostMapping(path = "calculate", consumes = "application/json")
    public BigDecimal calculate(@NotNull @Valid @RequestBody InvoiceDto invoiceDto) {
        return feeService.calculate(invoiceDto);
    }
}