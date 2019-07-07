package com.lmartino.bank.rest;

import com.google.gson.Gson;
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


public class AccountRestApi {

    public static void init(final CreateAccountUseCase createAccountUseCase, final GetAccountUseCase getAccountUseCase){

        post("/api/accounts", (request, response) -> {
            response.type("application/json");
            CreateAccountDto account = new Gson().fromJson(request.body(), CreateAccountDto.class);
            Account createdAccount = createAccountUseCase.compose(account.getName(), account.getBalance());
            AccountDto createdAccountDto = toDto(createdAccount);
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
                account.getCreatedAt()
        );
    }

}
