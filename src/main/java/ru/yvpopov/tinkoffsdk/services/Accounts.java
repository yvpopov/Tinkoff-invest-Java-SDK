package ru.yvpopov.tinkoffsdk.services;

import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;
import ru.yvpopov.tinkoffsdk.services.helpers.Service;
import ru.tinkoff.piapi.contract.v1.*;
import ru.yvpopov.tinkoffsdk.Communication;

public class Accounts extends Service {

    //UsersServiceGrpc.UsersServiceBlockingStub stub = UsersServiceGrpc.newBlockingStub(getCommunication().getChannel());
    //io.grpc.stub.AbstractBlockingStub stub;
    public Accounts(Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.UsersServiceGrpc.class);
    }

    /**
     *
     * @return Информация о счёте.
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetAccountsResponse GetAccounts() throws ServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetAccountsMethod(),
                GetAccountsRequest.newBuilder().build()
        );
    }

    /**
     *
     * @param account_id Идентификатор счёта пользователя.
     * @return Маржинальные показатели по счёту.
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetMarginAttributesResponse GetMarginAttributes(String account_id) throws ServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetMarginAttributesMethod(),
                GetMarginAttributesRequest.newBuilder().setAccountId(account_id).build()
        );
    }

    /**
     *
     * @return Текущие лимиты пользователя.
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetUserTariffResponse GetUserTariff() throws ServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetUserTariffMethod(),
                GetUserTariffRequest.newBuilder().build()
        );
    }

    /**
     *
     * @return Информация о пользователе.
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetInfoResponse GetInfo() throws ServiceException {
        return CallMethod(
                UsersServiceGrpc.getGetInfoMethod(),
                GetInfoRequest.newBuilder().build()
        );
    }

}
