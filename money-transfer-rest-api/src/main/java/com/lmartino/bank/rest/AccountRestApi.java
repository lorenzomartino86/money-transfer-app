package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.lmartino.bank.domain.exception.AccountNameConflictException;
import com.lmartino.bank.domain.exception.UnknownAccountException;
import com.lmartino.bank.domain.exception.UnknownCurrencyCodeException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.AccountTransfer;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountTransfersUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.rest.converter.DtoConverter;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.CreateAccountDto;
import com.lmartino.bank.rest.dto.ErrorDto;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;


public class AccountRestApi implements RestApi{
    private CreateAccountUseCase createAccountUseCase;
    private GetAccountUseCase getAccountUseCase;
    private GetAccountTransfersUseCase getAccountTransfersUseCase;

    @Inject
    public AccountRestApi(final CreateAccountUseCase createAccountUseCase,
                          final GetAccountUseCase getAccountUseCase,
                          final GetAccountTransfersUseCase getAccountTransfersUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.getAccountTransfersUseCase = getAccountTransfersUseCase;
    }

    public void init(){

        post("/api/accounts", (request, response) -> {
            response.type("application/json");
            CreateAccountDto account = new Gson().fromJson(request.body(), CreateAccountDto.class);
            Account createdAccount = createAccountUseCase.compose(account.getName(), account.getBalance(), account.getCurrency());
            AccountDto createdAccountDto = DtoConverter.accountDto(createdAccount);
            response.status(201);
            return new Gson().toJson(createdAccountDto);
        });

        get("/api/accounts/:id", (request, response) -> {
            response.type("application/json");
            Account account = getAccountUseCase.compose(request.params(":id"));
            AccountDto accountDto = DtoConverter.accountDto(account);
            return new Gson().toJson(accountDto);
        });

        get("/api/accounts/:id/transfers", (request, response) -> {
            response.type("application/json");
            List<AccountTransfer> transfers = getAccountTransfersUseCase.compose(request.params(":id"));
            return new Gson().toJson(transfers.stream().map(DtoConverter::accountTransferDto).collect(Collectors.toList()));
        });

        get("/api/accounts", (request, response) -> {
            response.type("application/json");
            List<Account> accounts = getAccountUseCase.compose();
            List<AccountDto> accountDtos = accounts.stream().map(DtoConverter::accountDto).collect(Collectors.toList());
            return new Gson().toJson(accountDtos);
        });

        exception(UnknownAccountException.class, (exception, request, response) -> {
            response.status(404);
            response.body(new Gson().toJson(new ErrorDto("account-id", exception.getMessage())));
        });

        exception(AccountNameConflictException.class, (exception, request, response) -> {
            response.status(409);
            response.body(new Gson().toJson(new ErrorDto("account-name", exception.getMessage())));
        });

        exception(UnknownCurrencyCodeException.class, (exception, request, response) -> {
            response.status(400);
            response.body(new Gson().toJson(new ErrorDto("currency", exception.getMessage())));
        });

    }

}
