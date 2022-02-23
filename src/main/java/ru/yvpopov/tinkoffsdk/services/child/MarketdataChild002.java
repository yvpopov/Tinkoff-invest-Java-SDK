package ru.yvpopov.tinkoffsdk.services.child;

import com.google.protobuf.Timestamp;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.GetCandlesResponse;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.Instruments;
import ru.yvpopov.tinkoffsdk.services.helpers.TinkoffServiceException;
import ru.yvpopov.tinkoffsdk.tools.convertors;
import ru.yvpopov.tools.ConvertDateTime;

/**
 * Добавлено получение цены по инструменту на определенное время
 * @author yvpop
 */
public class MarketdataChild002 extends MarketdataChild001{

    private final InstrumentsChild001 InstrumentSevice;
    
    public MarketdataChild002(Communication communication, InstrumentsChild001 InstrumentSevice) {
        super(communication);
        this.InstrumentSevice = InstrumentSevice;
    }
    
    /**
     *
     * @param typeinstrument - тип инструмента
     * @param ticker - Тикер (если не найдено точного соответствия, то будет произведен поиск тикера по первым символам при однозначном совпадении)
     * @param time - Время
     * @return Цена по инструменту на заданное время
     * @throws TinkoffServiceException
     */
    public Quotation GetPriceToTime(Instruments.TypeInstrument typeinstrument, String ticker, Timestamp time) throws TinkoffServiceException {
        String Figi = InstrumentSevice.GetFigiByTicker(typeinstrument, ticker, InstrumentsChild001.TickerFindMode.Soft);
        return this.GetPriceToTime(Figi, time);
    }

    /**
     *
     * @param figi - Figi по инструменту
     * @param time - Время
     * @return Цена по инструменту на заданное время
     * @throws TinkoffServiceException
     */
    public Quotation GetPriceToTime(String figi, Timestamp time) throws TinkoffServiceException {
        GetCandlesResponse gcr = GetCandles(figi, ConvertDateTime.getInstace(time).minus(1, ChronoUnit.DAYS).toTimestamp(), time, CandleInterval.CANDLE_INTERVAL_1_MIN);
        if (gcr.getCandlesCount() > 0) {
            return gcr.getCandlesList().get(gcr.getCandlesCount()-1).getClose();
        }
        gcr = GetCandles(figi, ConvertDateTime.getInstace(time).minus(1, ChronoUnit.WEEKS).toTimestamp(), time, CandleInterval.CANDLE_INTERVAL_HOUR);
        if (gcr.getCandlesCount() > 0) {
            return gcr.getCandlesList().get(gcr.getCandlesCount()-1).getClose();
        }
        gcr = GetCandles(figi, ConvertDateTime.getInstace(time).minus(1, ChronoUnit.WEEKS).toTimestamp(), time, CandleInterval.CANDLE_INTERVAL_DAY);
        if (gcr.getCandlesCount() > 0) {
            return gcr.getCandlesList().get(gcr.getCandlesCount()-1).getClose();
        }
        return convertors.BigDecimaltoQuotation(BigDecimal.ZERO);
    }
    
}
