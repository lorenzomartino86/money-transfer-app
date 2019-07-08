package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.lmartino.bank.domain.exception.UnknownAccountException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.CreateAccountDto;
import com.lmartino.bank.rest.dto.ErrorDto;

import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;


public class AccountRestApi implements RestApi{
    private CreateAccountUseCase createAccountUseCase;
    private GetAccountUseCase getAccountUseCase;

    @Inject
    public AccountRestApi(CreateAccountUseCase createAccountUseCase, GetAccountUseCase getAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
    }

    public void init(){

        post("/api/accounts", (request, response) -> {
            response.type("application/json");
            CreateAccountDto account = new Gson().fromJson(request.body(), CreateAccountDto.class);
            Account createdAccount = createAccountUseCase.compose(account.getName(), account.getBalance(), account.getCurrency());
            AccountDto createdAccountDto = toDto(createdAccount);
            response.status(201);
            return new Gson().toJson(createdAccountDto);
        });

        get("/api/accounts/:id", (request, response) -> {
            response.type("application/json");
            Account account = getAccountUseCase.compose(request.params(":id"));
            AccountDto accountDto = toDto(account);
            return new Gson().toJson(accountDto);
        });

        get("/api/accounts", (request, response) -> {
            response.type("application/json");
            List<Account> accounts = getAccountUseCase.compose();
            List<AccountDto> accountDtos = accounts.stream().map(AccountRestApi::toDto).collect(Collectors.toList());
            return new Gson().toJson(accountDtos);
        });

        exception(UnknownAccountException.class, (exception, request, response) -> {
            response.status(404);
            response.body(new Gson().toJson(new ErrorDto("account-id", exception.getMessage())));
        });

    }


    public static void init(final CreateAccountUseCase createAccountUseCase, final GetAccountUseCase getAccountUseCase){

        post("/api/accounts", (request, response) -> {
            response.type("application/json");
            CreateAccountDto account = new Gson().fromJson(request.body(), CreateAccountDto.class);
            Account createdAccount = createAccountUseCase.compose(account.getName(), account.getBalance(), account.getCurrency());
            AccountDto createdAccountDto = toDto(createdAccount);
            response.status(201);
            return new Gson().toJson(createdAccountDto);
        });

        get("/api/accounts/:id", (request, response) -> {
            response.type("application/json");
            Account account = getAccountUseCase.compose(request.params(":id"));
            AccountDto accountDto = toDto(account);
            return new Gson().toJson(accountDto);
        });

        get("/api/accounts", (request, response) -> {
            response.type("application/json");
            List<Account> accounts = getAccountUseCase.compose();
            List<AccountDto> accountDtos = accounts.stream().map(AccountRestApi::toDto).collect(Collectors.toList());
            return new Gson().toJson(accountDtos);
        });

        exception(UnknownAccountException.class, (exception, request, response) -> {
            response.status(404);
            response.body(new Gson().toJson(new ErrorDto("account-id", exception.getMessage())));
        });

    }

    private static AccountDto toDto(Account account) {
        return AccountDto.of(
                account.getId().getValue(),
                account.getName(),
                account.getBalance().getMoney(),
                account.getCreatedAt(),
                account.getCurrency().getValue()
        );
    }

}
