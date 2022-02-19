package ru.yvpopov.tinkoffsdk.services;

import java.math.BigDecimal;
import ru.tinkoff.piapi.contract.v1.*;
import javax.annotation.Nonnull;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;

public class Sandbox extends ServiceBase {

    public Sandbox(@Nonnull final Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.class);
    }

    /**
     *
     * @return Номер открытого счёта в песочнице.
     * @throws ServiceException
     */
    public OpenSandboxAccountResponse OpenSandboxAccount() throws ServiceException {
        var build = OpenSandboxAccountRequest.newBuilder();
        return CallMethod(
                SandboxServiceGrpc.getOpenSandboxAccountMethod(),
                build.build()
        );
    }

    /**
     *
     * @return Метод получения счетов в песочнице.
     * @throws ServiceException
     */
    public GetAccountsResponse GetSandboxAccounts() throws ServiceException {
        var build = ru.tinkoff.piapi.contract.v1.GetAccountsRequest.newBuilder();
        return CallMethod(
                SandboxServiceGrpc.getGetSandboxAccountsMethod(),
                build.build()
        );
    }

    /**
     *
     * @param account_id - Номер счёта
     * @return Метод закрытия счёта в песочнице.
     * @throws ServiceException
     */
    public CloseSandboxAccountResponse CloseSandboxAccount(@Nonnull final String account_id) throws ServiceException {
        var build = CloseSandboxAccountRequest.newBuilder()
                .setAccountId(account_id);        
        return CallMethod(
                SandboxServiceGrpc.getCloseSandboxAccountMethod(),
                build.build()
        );
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param quantity Количество лотов.
     * @param price Цена лота.
     * @param direction Направление операции.
     * @param account_id Номер счёта.
     * @param order_type Тип заявки.
     * @param order_id Идентификатор запроса выставления поручения для целей
     * идемпотентности. Максимальная длина 36 символов.
     * @return Информация о выставлении поручения.
     * @throws ServiceException
     */
    public PostOrderResponse PostSandboxOrder(
            @Nonnull final String figi,
            @Nonnull final long quantity,
            @Nonnull final BigDecimal price,
            @Nonnull final OrderDirection direction,
            @Nonnull final String account_id,
            @Nonnull final OrderType order_type,
            String order_id) throws ServiceException {
        var build = PostOrderRequest.newBuilder();
        build.setFigi(figi)
                .setQuantity(quantity)
                .setPrice(Common.BigDecimaltoQuotation(price))
                .setDirection(direction)
                .setAccountId(account_id)
                .setOrderType(order_type);
        if (order_id != null) {
            build.setOrderId(order_id);
        }
        return CallMethod(
                SandboxServiceGrpc.getPostSandboxOrderMethod(),
                build.build()
        );
    }

   /**
     *
     * @param account_id Номер счёта.
     * @param order_id Идентификатор заявки.
     * @return Список активных торговых поручений.
     * @throws ServiceException
     */
    public  CancelOrderResponse CancelSandboxOrder(
            @Nonnull final String account_id,
            @Nonnull final String order_id
    ) throws ServiceException {
        var build = CancelOrderRequest.newBuilder()
                .setAccountId(account_id)
                .setOrderId(order_id);
        return CallMethod(
                OrdersServiceGrpc.getCancelOrderMethod(),
                build.build()
        );
    }

    /**
     *
     * @param account_id - Номер счёта.
     * @param order_id - Идентификатор заявки.
     * @return Информация о торговом поручении.
     * @throws ServiceException
     */
    public OrderState GetSandboxOrderState(
            @Nonnull final String account_id,
            @Nonnull final String order_id
    ) throws ServiceException {
        var build =  GetOrderStateRequest.newBuilder()
                .setAccountId(account_id)
                .setOrderId(order_id);
        return CallMethod(
                OrdersServiceGrpc.getGetOrderStateMethod(),
                build.build()
        );
    }

    
    

}
