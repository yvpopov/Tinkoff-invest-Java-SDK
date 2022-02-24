package ru.yvpopov.tinkoffsdk.services;

import java.math.BigDecimal;
import ru.tinkoff.piapi.contract.v1.*;
import javax.annotation.Nonnull;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.helpers.TinkoffServiceException;
import static ru.yvpopov.tinkoffsdk.tools.convertors.*;

public class StopOrders extends ServiceBase {
    
    public StopOrders(@Nonnull final Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.StopOrdersServiceGrpc.class);
    }
    
    /**
     *
     * @param figi Figi-идентификатор инструмента
     * @param quantity Количество лотов
     * @param price Цена лота
     * @param stop_price Стоп-цена заявки
     * @param direction Направление операции
     * @param account_id Номер счёта
     * @param expiration_type Тип экспирации заявки
     * @param stop_order_type Тип заявки
     * @param expire_date Дата и время окончания действия стоп-заявки в часовом поясе UTC. Для ExpirationType = GoodTillDate заполнение обязательно.
     * @return Результат выставления стоп-заявки.
     * @throws TinkoffServiceException
     */
    public PostStopOrderResponse PostOrder(
            @Nonnull final String figi,
            @Nonnull final long quantity,
            @Nonnull final BigDecimal price,
            @Nonnull final BigDecimal stop_price,
            @Nonnull final StopOrderDirection direction,
            @Nonnull final String account_id,
            @Nonnull final StopOrderExpirationType expiration_type,
            @Nonnull final StopOrderType stop_order_type,
            com.google.protobuf.Timestamp expire_date) throws TinkoffServiceException {
        PostStopOrderRequest.Builder build = PostStopOrderRequest.newBuilder();
        build.setFigi(figi)
                .setQuantity(quantity)
                .setPrice(BigDecimaltoQuotation(price))
                .setStopPrice(BigDecimaltoQuotation(stop_price))
                .setDirection(direction)
                .setAccountId(account_id)
                .setExpirationType(expiration_type)
                .setStopOrderType(stop_order_type);
        if (expire_date != null) {
            build.setExpireDate(expire_date);
        }
        return CallMethod(
                StopOrdersServiceGrpc.getPostStopOrderMethod(),
                build.build()
        );
    }
    
    /**
     *
     * @param account_id Номер счёта.
     * @return Список активных стоп-заявок.
     * @throws TinkoffServiceException
     */
    public GetStopOrdersResponse GetStopOrders(
            @Nonnull final String account_id
    ) throws TinkoffServiceException {
        GetStopOrdersRequest.Builder build = GetStopOrdersRequest.newBuilder()
                .setAccountId(account_id);
        return CallMethod(
                StopOrdersServiceGrpc.getGetStopOrdersMethod(),
                build.build()
        );
    }

    /**
     *
     * @param account_id Номер счёта.
     * @return Результат отмены выставленной стоп-заявки.
     * @throws TinkoffServiceException
     */
    public CancelStopOrderResponse CancelStopOrder(
            @Nonnull final String account_id
    ) throws TinkoffServiceException {
        CancelStopOrderRequest.Builder build = CancelStopOrderRequest.newBuilder()
                .setAccountId(account_id);
        return CallMethod(
                StopOrdersServiceGrpc.getCancelStopOrderMethod(),
                build.build()
        );
    }}
