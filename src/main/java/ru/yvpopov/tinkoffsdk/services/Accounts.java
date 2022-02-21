package ru.yvpopov.tinkoffsdk.services;

import javax.annotation.Nonnull;
import ru.yvpopov.tinkoffsdk.services.helpers.TinkoffServiceException;
import ru.tinkoff.piapi.contract.v1.*;
import ru.yvpopov.tinkoffsdk.Communication;

public class Accounts extends ServiceBase {

    public Accounts(Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.UsersServiceGrpc.class);
    }

    /**
     *
     * @return Информация о счёте.
     * @throws TinkoffServiceException
     */
    public GetAccountsResponse GetAccounts() throws TinkoffServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetAccountsMethod(),
                GetAccountsRequest.newBuilder().build()
        );
    }

    /**
     *
     * @param account_id Идентификатор счёта пользователя.
     * @return Маржинальные показатели по счёту.
     * @throws TinkoffServiceException
     * Может возникать исключение если маржинальная торговля для счета выключена
     */
    public GetMarginAttributesResponse GetMarginAttributes(@Nonnull final String account_id) throws TinkoffServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetMarginAttributesMethod(),
                GetMarginAttributesRequest.newBuilder().setAccountId(account_id).build()
        );
    }

    /**
     *
     * @return Текущие лимиты пользователя.
     * @throws TinkoffServiceException
     */
    public GetUserTariffResponse GetUserTariff() throws TinkoffServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetUserTariffMethod(),
                GetUserTariffRequest.newBuilder().build()
        );
    }

    /**
     *
     * @return Информация о пользователе.
     * @throws TinkoffServiceException
     */
    public GetInfoResponse GetInfo() throws TinkoffServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetInfoMethod(),
                GetInfoRequest.newBuilder().build()
        );
    }

}
