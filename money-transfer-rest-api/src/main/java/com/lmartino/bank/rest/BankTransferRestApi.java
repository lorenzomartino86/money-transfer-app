package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.lmartino.bank.domain.exception.IllegalTransferCurrencyException;
import com.lmartino.bank.domain.exception.InsufficientBalanceException;
import com.lmartino.bank.domain.exception.UnknownCurrencyCodeException;
import com.lmartino.bank.domain.model.Currency;
import com.lmartino.bank.domain.model.Money;
import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.domain.usecase.MakeTransferUseCase;
import com.lmartino.bank.rest.converter.DtoConverter;
import com.lmartino.bank.rest.dto.ErrorDto;
import com.lmartino.bank.rest.dto.MakeTransferDto;
import com.lmartino.bank.rest.dto.TransferDto;

import static spark.Spark.exception;
import static spark.Spark.post;


public class BankTransferRestApi implements RestApi{
    private final MakeTransferUseCase makeTransferUseCase;

    @Inject
    public BankTransferRestApi(MakeTransferUseCase makeTransferUseCase) {
        this.makeTransferUseCase = makeTransferUseCase;
    }

    @Override
    public void init(){

        post("/api/transfers", (request, response) -> {
            response.type("application/json");
            MakeTransferDto transferDto = new Gson().fromJson(request.body(), MakeTransferDto.class);
            Transfer transfer = makeTransferUseCase.compose(transferDto.getFromAccountId(),
                    transferDto.getToAccountId(),
                    Money.of(transferDto.getAmount(), Currency.of(transferDto.getCurrency())),
                    transferDto.getDescription());
            TransferDto createdTransferDto = DtoConverter.transferDto(transfer);
            response.status(201);
            return new Gson().toJson(createdTransferDto);
        });

        exception(InsufficientBalanceException.class, (exception, request, response) -> {
            response.status(422);
            response.body(new Gson().toJson(new ErrorDto("balance", exception.getMessage())));
        });

        exception(UnknownCurrencyCodeException.class, (exception, request, response) -> {
            response.status(400);
            response.body(new Gson().toJson(new ErrorDto("currency", exception.getMessage())));
        });

        exception(IllegalTransferCurrencyException.class, (exception, request, response) -> {
            response.status(400);
            response.body(new Gson().toJson(new ErrorDto("currency", exception.getMessage())));
        });

    }

}
