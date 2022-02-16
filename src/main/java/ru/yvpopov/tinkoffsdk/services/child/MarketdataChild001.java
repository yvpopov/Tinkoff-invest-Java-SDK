package ru.yvpopov.tinkoffsdk.services.child;

import com.google.protobuf.Timestamp;
import java.time.temporal.ChronoUnit;
import javax.annotation.Nonnull;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.GetCandlesResponse;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;
import ru.yvpopov.tools.ConvertDateTime;

/**
 * Метод GetCandles можно задавать любой период from, to при задании from = null
 * - начало истории при задании to = null - текущий момент
 *
 * @author yvpop
 */
public class MarketdataChild001 extends ru.yvpopov.tinkoffsdk.services.Marketdata {

    public MarketdataChild001(Communication communication) {
        super(communication);
    }

    /**
     *
     * Описание лимитов. источник:
     * https://tinkoff.github.io/investAPI/load_history/
     *
     * @param to - конец периода
     * @param interval - интервал свечи
     * @return допустимый значение Timestamp начала периода
     */
    private Timestamp getFromWithLimit(Timestamp to, ru.tinkoff.piapi.contract.v1.CandleInterval interval) {
        switch (interval) {
            case CANDLE_INTERVAL_1_MIN -> {
                return new ConvertDateTime(to).minus(1, ChronoUnit.DAYS).toTimestamp();
            }
            case CANDLE_INTERVAL_5_MIN -> {
                return new ConvertDateTime(to).minus(1, ChronoUnit.DAYS).toTimestamp();
            }
            case CANDLE_INTERVAL_15_MIN -> {
                return new ConvertDateTime(to).minus(1, ChronoUnit.DAYS).toTimestamp();
            }
            case CANDLE_INTERVAL_HOUR -> {
                return new ConvertDateTime(to).minus(1, ChronoUnit.WEEKS).toTimestamp();
            }
            case CANDLE_INTERVAL_DAY -> {
                return new ConvertDateTime(to).minus(1, ChronoUnit.YEARS).toTimestamp();
            }
        }
        return to;
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param interval Интервал запрошенных свечей. (по умолчанию
     * CANDLE_INTERVAL_DAY)
     * @return Cвечи по инструменту за весь период
     * @throws ServiceException
     */
    public GetCandlesResponse GetCandles(@Nonnull final String figi, CandleInterval interval) throws ServiceException {
        return this.GetCandles(figi, null, null, interval);
    }

    private Timestamp getCandleMinus(Timestamp time, CandleInterval interval) {
        return getCandleMinus(time, interval, 1);
    }

    private Timestamp getCandleMinus(Timestamp time, CandleInterval interval, int count) {
        switch (interval) {
            case CANDLE_INTERVAL_1_MIN -> {
                return new ConvertDateTime(time).minus(1 * count, ChronoUnit.MINUTES).toTimestamp();
            }
            case CANDLE_INTERVAL_5_MIN -> {
                return new ConvertDateTime(time).minus(5 * count, ChronoUnit.MINUTES).toTimestamp();
            }
            case CANDLE_INTERVAL_15_MIN -> {
                return new ConvertDateTime(time).minus(15 * count, ChronoUnit.MINUTES).toTimestamp();
            }
            case CANDLE_INTERVAL_HOUR -> {
                return new ConvertDateTime(time).minus(1 * count, ChronoUnit.HOURS).toTimestamp();
            }
            case CANDLE_INTERVAL_DAY -> {
                return new ConvertDateTime(time).minus(1 * count, ChronoUnit.DAYS).toTimestamp();
            }
        }
        return time;
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param from Начало запрашиваемого периода в часовом поясе UTC.
     * @param to Окончание запрашиваемого периода в часовом поясе UTC.
     * @param interval Интервал запрошенных свечей. (по умолчанию
     * CANDLE_INTERVAL_DAY)
     * @return Cвечи по инструменту
     * @throws ru.yvpopov.tinkoffsdk.services.helpers.ServiceException
     */
    @Override
    public GetCandlesResponse GetCandles(@Nonnull final String figi, Timestamp from, Timestamp to, CandleInterval interval) throws ServiceException {
        return this.GetCandles(figi, from, to, interval, COUNTZERO_DEFAULT);
    }

    /**
     * Колличество пустых ответов, после которых считаем что история закончилась (необходимо для таймфреймов ниже 1 часа)
     */
    private static final int COUNTZERO_DEFAULT = 7;

    private GetCandlesResponse GetCandles(@Nonnull final String figi, Timestamp from, Timestamp to, CandleInterval interval, int countzero) throws ServiceException {
        if (to == null) {
            to = new ConvertDateTime().toTimestamp();
        }
        Timestamp maxfrom = getFromWithLimit(to, interval);
        if (from != null && ConvertDateTime.Compare(from, maxfrom) >= 0) {
            return super.GetCandles(figi, from, to, interval);
        } else {
            GetCandlesResponse gcr1 = super.GetCandles(figi, maxfrom, to, interval);
            if (gcr1.getCandlesCount() > 0 || countzero > 0) {
                if (gcr1.getCandlesCount() == 0)
                    countzero--;
                else 
                    countzero = COUNTZERO_DEFAULT;
                GetCandlesResponse gcr0 = this.GetCandles(figi, from, getCandleMinus(maxfrom, interval), interval, countzero);
                gcr0 = gcr0.toBuilder().addAllCandles(gcr1.getCandlesList()).build();
                return gcr0;
            } else {
                return gcr1;
            }
        }
    }
    
}
