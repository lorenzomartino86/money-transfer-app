package com.lmartino.bank.rest;

import com.google.gson.Gson;
import com.lmartino.bank.domain.exception.InsufficientBalanceException;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Transfer;
import com.lmartino.bank.domain.usecase.MakeTransferUseCase;
import com.lmartino.bank.rest.dto.ErrorDto;
import com.lmartino.bank.rest.dto.MakeTransferDto;
import com.lmartino.bank.rest.dto.TransferDto;

import static spark.Spark.exception;
import static spark.Spark.post;


public class BankTransferRestApi {

    public static void init(final MakeTransferUseCase makeTransferUseCase){

        post("/api/transfers", (request, response) -> {
            response.type("application/json");
            MakeTransferDto transferDto = new Gson().fromJson(request.body(), MakeTransferDto.class);
            Transfer transfer = makeTransferUseCase.compose(transferDto.getFromAccountId(),
                    transferDto.getToAccountId(),
                    Amount.of(transferDto.getAmount()),
                    transferDto.getDescription());
            TransferDto createdTransferDto = toDto(transfer);
            return new Gson().toJson(createdTransferDto);
        });

        exception(InsufficientBalanceException.class, (exception, request, response) -> {
            response.status(422);
            response.body(new Gson().toJson(new ErrorDto("balance", exception.getMessage())));
        });

    }

    private static TransferDto toDto(Transfer transfer) {
        return TransferDto.of(
                transfer.getId().getValue(),
                transfer.getFromAccount().getId().getValue(),
                transfer.getToAccount().getId().getValue(),
                transfer.getDescription(),
                transfer.getAmount().getMoney(),
                transfer.getCreatedAt()
        );
    }

}
