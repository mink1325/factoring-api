package com.mkcode.factoring.rest;

import com.mkcode.factoring.model.ContractDto;
import com.mkcode.factoring.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping(path = "/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json")
    public void save(@NotNull @Valid @RequestBody ContractDto contractDto) {
        contractService.save(contractDto);
    }
}
