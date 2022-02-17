package ru.yvpopov.tinkoffsdk.services;

import java.math.BigDecimal;
import ru.tinkoff.piapi.contract.v1.*;

import javax.annotation.Nonnull;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;

public class Orders extends ServiceBase {

    public Orders(@Nonnull final Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.OrdersServiceGrpc.class);
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
    public PostOrderResponse PostOrder(
            @Nonnull final String figi,
            @Nonnull final long quantity,
            @Nonnull final BigDecimal price,
            @Nonnull final OrderDirection direction,
            @Nonnull final String account_id,
            @Nonnull final OrderType order_type,
            String order_id
    ) throws ServiceException {
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
                OrdersServiceGrpc.getPostOrderMethod(),
                build.build()
        );
    }

    /**
     *
     * @param account_id Номер счёта.
     * @param order_id Идентификатор заявки.
     * @return Результат отмены торгового поручения.
     * @throws ServiceException
     */
    public CancelOrderResponse CancelOrder(
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
     * @param account_id Номер счёта.
     * @param order_id Идентификатор заявки.
     * @return Информация о торговом поручении.
     * @throws ServiceException
     */
    public OrderState GetOrderState(
            @Nonnull final String account_id,
            @Nonnull final String order_id
    ) throws ServiceException {
        var build = GetOrderStateRequest.newBuilder()
                .setAccountId(account_id)
                .setOrderId(order_id);
        return CallMethod(
                OrdersServiceGrpc.getGetOrderStateMethod(),
                build.build()
        );
    }    
 
    /**
     *
     * @param account_id Номер счёта.
     * @return Список активных торговых поручений.
     * @throws ServiceException
     */
    public GetOrdersResponse GetOrders(
            @Nonnull final String account_id
    ) throws ServiceException {
        var build = GetOrdersRequest.newBuilder()
                .setAccountId(account_id);
        return CallMethod(
                OrdersServiceGrpc.getGetOrdersMethod(),
                build.build()
        );
    }        
}
