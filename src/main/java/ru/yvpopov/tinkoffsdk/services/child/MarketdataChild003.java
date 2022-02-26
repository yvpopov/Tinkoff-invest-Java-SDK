package ru.yvpopov.tinkoffsdk.services.child;

import com.google.protobuf.Timestamp;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.GetCandlesResponse;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.helpers.TinkoffServiceException;
import ru.yvpopov.tinkoffsdk.tools.MoneyQuatationHelper;
import static ru.yvpopov.tinkoffsdk.tools.MoneyQuatationHelper.QuotationtoBigDecimal;
import ru.yvpopov.tools.ConvertDateTime;

public class MarketdataChild003 extends MarketdataChild002 {

    public MarketdataChild003(Communication communication, InstrumentsChild001 InstrumentSevice) {
        super(communication, InstrumentSevice);
    }

    public static enum CandleIntervalExtended {
        CANDLE_INTERVAL_1_MIN,
        CANDLE_INTERVAL_5_MIN,
        CANDLE_INTERVAL_15_MIN,
        CANDLE_INTERVAL_HOUR,
        CANDLE_INTERVAL_DAY,
        CANDLE_INTERVAL_WEEK,
        CANDLE_INTERVAL_MONTH
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param interval Интервал запрошенных свечей. (по умолчанию
     * CANDLE_INTERVAL_DAY)
     * @return Cвечи по инструменту за весь период
     * @throws TinkoffServiceException
     */
    public List<HistoricCandle> GetCandlesExtended(@Nonnull final String figi, CandleIntervalExtended interval) throws TinkoffServiceException {
        return GetCandlesExtended(figi, null, null, interval);
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param from Начало запрашиваемого периода в часовом поясе UTC.
     * @param to Окончание запрашиваемого периода в часовом поясе UTC.
     * @param interval Интервал запрошенных свечей. (по умолчанию
     * CANDLE_INTERVAL_DAY)
     * @return Cвечи по инструменту
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.TinkoffServiceException
     */
    public List<HistoricCandle> GetCandlesExtended(@Nonnull final String figi, Timestamp from, Timestamp to, CandleIntervalExtended interval) throws TinkoffServiceException {
        switch (interval) {
            case CANDLE_INTERVAL_WEEK: {
                List<HistoricCandle> query = GetCandles(figi, from, to, CandleInterval.CANDLE_INTERVAL_DAY).getCandlesList();
                if (query.isEmpty()) {
                    return query;
                }
                List<HistoricCandle> result = new ArrayList<>();
                HistoricCandle candle_cur = query.get(0);
                int candle_no = 1;
                while (candle_no < query.size()) {
                    if (ConvertDateTime.getInstace(query.get(candle_no).getTime()).toZonedDateTime().getDayOfWeek() == DayOfWeek.MONDAY) {
                        result.add(candle_cur);
                        candle_cur = query.get(candle_no);
                    } else {
                        candle_cur = AddExtremumCandle(candle_cur, query.get(candle_no));
                    }
                    candle_no++;
                }
                result.add(candle_cur);
                return result;
            }
            case CANDLE_INTERVAL_MONTH: {
                List<HistoricCandle> query = GetCandles(figi, from, to, CandleInterval.CANDLE_INTERVAL_DAY).getCandlesList();
                if (query.isEmpty()) {
                    return query;
                }
                List<HistoricCandle> result = new ArrayList<>();
                HistoricCandle candle_cur = query.get(0);
                int mon = ConvertDateTime.getInstace(candle_cur.getTime()).toZonedDateTime().getMonthValue();
                int candle_no = 1;
                while (candle_no < query.size()) {
                    if (mon != ConvertDateTime.getInstace(query.get(candle_no).getTime()).toZonedDateTime().getMonthValue()) {
                        result.add(candle_cur);
                        candle_cur = query.get(candle_no);
                    } else {
                        candle_cur = AddExtremumCandle(candle_cur, query.get(candle_no));
                    }
                    mon = ConvertDateTime.getInstace(query.get(candle_no).getTime()).toZonedDateTime().getMonthValue();
                    candle_no++;
                }
                result.add(candle_cur);
                return result;
            }
            default: {
                return GetCandles(figi, from, to, CandleIntervalfromExtended(interval)).getCandlesList();
            }
        }
    }

    private HistoricCandle AddExtremumCandle(HistoricCandle candle1, HistoricCandle candle2) {
        if (ConvertDateTime.Compare(candle1.getTime(), candle2.getTime()) > 0) {
            HistoricCandle temp = candle1;
            candle1 = candle2;
            candle2 = temp;
        }
        candle1 = candle1.toBuilder().setClose(candle2.getClose()).build();
        if (MoneyQuatationHelper.Compare(candle2.getHigh(), candle1.getHigh()) > 0) {
            candle1 = candle1.toBuilder().setHigh(candle2.getHigh()).build();
        }
        if (MoneyQuatationHelper.Compare(candle2.getLow(), candle1.getLow()) < 0) {
            candle1 = candle1.toBuilder().setLow(candle2.getLow()).build();
        }
        return candle1;
    }

    private CandleInterval CandleIntervalfromExtended(CandleIntervalExtended interval) {
        switch (interval) {
            case CANDLE_INTERVAL_1_MIN:
                return CandleInterval.CANDLE_INTERVAL_1_MIN;
            case CANDLE_INTERVAL_5_MIN:
                return CandleInterval.CANDLE_INTERVAL_5_MIN;
            case CANDLE_INTERVAL_15_MIN:
                return CandleInterval.CANDLE_INTERVAL_15_MIN;
            case CANDLE_INTERVAL_HOUR:
                return CandleInterval.CANDLE_INTERVAL_HOUR;
            default:
                return CandleInterval.CANDLE_INTERVAL_DAY;
        }
    }

}
