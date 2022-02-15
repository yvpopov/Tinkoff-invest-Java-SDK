package ru.yvpopov.tinkoffsdk.services;

import java.util.List;
import javax.annotation.Nonnull;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.tinkoff.piapi.contract.v1.*;
import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;

public class Marketdata extends ServiceBase {

    public Marketdata(@Nonnull final Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.MarketDataServiceGrpc.class);
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param from Начало запрашиваемого периода в часовом поясе UTC.
     * @param to Окончание запрашиваемого периода в часовом поясе UTC.
     * @param interval Интервал запрошенных свечей. (по умолчанию CANDLE_INTERVAL_DAY)
     * @return Cвечи по инструменту
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetCandlesResponse GetCandles(@Nonnull final String figi, @Nonnull final com.google.protobuf.Timestamp from, @Nonnull final com.google.protobuf.Timestamp to, CandleInterval interval) throws ServiceException {
        var build = GetCandlesRequest.newBuilder();
        build.setFigi(figi);
        build.setFrom(from);
        build.setTo(to);
        if (interval == null) {
            interval = CandleInterval.CANDLE_INTERVAL_DAY;
        }
        build.setInterval(interval);
        return CallMethod(
                MarketDataServiceGrpc.getGetCandlesMethod(),
                build.build()
        );
    }

    /**
     *
     * @param figis Массив figi-идентификаторов инструментов.
     * @return Список последних цен
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetLastPricesResponse GetLastPrices(@Nonnull final List<String> figis) throws ServiceException {
        var build = GetLastPricesRequest.newBuilder();
        for (String figi : figis) {
            build.addFigi(figi);
        }
        return CallMethod(
                MarketDataServiceGrpc.getGetLastPricesMethod(),
                build.build()
        );
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param depth Глубина стакана.
     * @return Cтакана по инструменту
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetOrderBookResponse GetOrderBook(@Nonnull final String figi, int depth) throws ServiceException {
        var build = GetOrderBookRequest.newBuilder();
        build.setFigi(figi);
        build.setDepth(depth);
        return CallMethod(
                MarketDataServiceGrpc.getGetOrderBookMethod(),
                build.build()
        );
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @return Статус торгов по инструменту
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    public GetTradingStatusResponse GetTradingStatus(@Nonnull final String figi) throws ServiceException {
        var build = GetTradingStatusRequest.newBuilder();
        build.setFigi(figi);
        return CallMethod(
                MarketDataServiceGrpc.getGetTradingStatusMethod(),
                build.build()
        );
    }

}
