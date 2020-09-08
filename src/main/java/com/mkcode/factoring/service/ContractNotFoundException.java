package com.mkcode.factoring.service;

import java.time.LocalDate;

import static java.lang.String.format;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(String contractNo, LocalDate date) {
        super(format("No active contract %s for date %tF was found.", contractNo, date));
    }
}
