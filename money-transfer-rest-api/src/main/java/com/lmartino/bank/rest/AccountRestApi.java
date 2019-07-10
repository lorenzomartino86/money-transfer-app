package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.lmartino.bank.domain.exception.UnknownAccountException;
import com.lmartino.bank.domain.exception.UnknownCurrencyCodeException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.AccountTransfer;
import com.lmartino.bank.domain.usecase.CreateAccountUseCase;
import com.lmartino.bank.domain.usecase.GetAccountTransfersUseCase;
import com.lmartino.bank.domain.usecase.GetAccountUseCase;
import com.lmartino.bank.rest.dto.AccountDto;
import com.lmartino.bank.rest.dto.AccountTransferDto;
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

        get("/api/accounts/:id/transfers", (request, response) -> {
            response.type("application/json");
            List<AccountTransfer> transfers = getAccountTransfersUseCase.compose(request.params(":id"));
            return new Gson().toJson(transfers.stream().map(AccountRestApi::toDto).collect(Collectors.toList()));
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

        exception(UnknownCurrencyCodeException.class, (exception, request, response) -> {
            response.status(400);
            response.body(new Gson().toJson(new ErrorDto("currency", exception.getMessage())));
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

    private static AccountTransferDto toDto(AccountTransfer accountTransfer) {
        return AccountTransferDto.of(
                accountTransfer.getId().getValue(),
                accountTransfer.getToAccountId().getValue(),
                accountTransfer.getAmount().getMoney(),
                accountTransfer.getDescription(),
                accountTransfer.getCreatedAt()
        );
    }

}
