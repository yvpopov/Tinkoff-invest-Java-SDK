package ru.yvpopov.tinkoffsdk.services;

import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;
import ru.yvpopov.tinkoffsdk.services.helpers.Service;
import ru.tinkoff.piapi.contract.v1.*;
import ru.yvpopov.tinkoffsdk.Communication;

public class Instruments extends Service {

    public Instruments(Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.class);
    }

    /**
     *
     * @param Exchange Наименование биржи или расчетного календаря. 
     * Если не передаётся, возвращается информация по всем доступным торговым площадкам.
     * @param from Начало периода по часовому поясу UTC.
     * @param to Окончание периода по часовому поясу UTC.
     * @return Список торговых площадок
     * @throws ServiceException
     */
    public TradingSchedulesResponse TradingSchedules(String Exchange, com.google.protobuf.Timestamp from, com.google.protobuf.Timestamp to) throws ServiceException {
        var build = TradingSchedulesRequest.newBuilder();
        if (Exchange != null) build.setExchange(Exchange);
        if (from != null) build.setFrom(from);
        if (to != null) build.setTo(to);
        return CallMethod(
                InstrumentsServiceGrpc.getTradingSchedulesMethod(),
                build.build()
        );
    }

    private InstrumentRequest instrumentrequest(InstrumentIdType id_type, String class_code, String id) {
        var build = InstrumentRequest.newBuilder();
        if (id_type == null) id_type = InstrumentIdType.INSTRUMENT_ID_UNSPECIFIED;
        build.setIdType(id_type);
        if (class_code != null) build.setClassCode(class_code);
        if (id != null) build.setClassCode(id);
        return build.build();
    }

    /**
     *
     * @param id_type Тип идентификатора инструмента. Возможные значения: figi, ticker.
     * @param class_code Идентификатор class_code.
     * @param id Идентификатор запрашиваемого инструмента.
     * @return Информация об облигации.
     * @throws ServiceException
     */
    public BondResponse BondBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getBondByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    /**
     *
     * @param id_type Тип идентификатора инструмента. Возможные значения: figi, ticker.
     * @param class_code Идентификатор class_code.
     * @param id Идентификатор запрашиваемого инструмента.
     * @return Данные по валюте.
     * @throws ServiceException
     */
    public CurrencyResponse CurrencyBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getCurrencyByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    /**
     *
     * @param id_type Тип идентификатора инструмента. Возможные значения: figi, ticker.
     * @param class_code Идентификатор class_code.
     * @param id Идентификатор запрашиваемого инструмента.
     * @return Данные по фонду.
     * @throws ServiceException
     */    
    public EtfResponse EtfBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getEtfByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    /**
     *
     * @param id_type Тип идентификатора инструмента. Возможные значения: figi, ticker.
     * @param class_code Идентификатор class_code.
     * @param id Идентификатор запрашиваемого инструмента.
     * @return Данные по фьючерсу.
     * @throws ServiceException
     */
    public FutureResponse FutureBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getFutureByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    /**
     *
     * @param id_type Тип идентификатора инструмента. Возможные значения: figi, ticker.
     * @param class_code Идентификатор class_code.
     * @param id Идентификатор запрашиваемого инструмента.
     * @return Данные по акции.
     * @throws ServiceException
     */
    public ShareResponse ShareBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getShareByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    /**
     *
     * @param id_type Тип идентификатора инструмента. Возможные значения: figi, ticker.
     * @param class_code Идентификатор class_code.
     * @param id Идентификатор запрашиваемого инструмента.
     * @return Данные по инструменту.
     * @throws ServiceException
     */
    public InstrumentResponse GetInstrumentBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getGetInstrumentByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }
    
    private InstrumentsRequest instrumentsrequest(InstrumentStatus instrumentStatus) {
        if (instrumentStatus == null) 
            instrumentStatus = InstrumentStatus.INSTRUMENT_STATUS_UNSPECIFIED;
        return InstrumentsRequest.newBuilder().setInstrumentStatus(instrumentStatus).build();
    }
    
    /**
     *
     * @param instrumentStatus Статус запрашиваемых инструментов.
     * Возможные значения: 
     * INSTRUMENT_STATUS_UNSPECIFIED	Значение не определено.
     * INSTRUMENT_STATUS_BASE	Базовый список инструментов (по умолчанию). Инструменты доступные для торговли через TINKOFF INVEST API.
     * INSTRUMENT_STATUS_ALL	Список всех инструментов.
     * @return Список облигаций.
     * @throws ServiceException
     */
    public BondsResponse Bonds(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getBondsMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }

    /**
     *
     * @param instrumentStatus Статус запрашиваемых инструментов.
     * Возможные значения: 
     * INSTRUMENT_STATUS_UNSPECIFIED	Значение не определено.
     * INSTRUMENT_STATUS_BASE	Базовый список инструментов (по умолчанию). Инструменты доступные для торговли через TINKOFF INVEST API.
     * INSTRUMENT_STATUS_ALL	Список всех инструментов.
     * @return Данные по валютам.
     * @throws ServiceException
     */
    public CurrenciesResponse Currencys(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getCurrenciesMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }

    /**
     *
     * @param instrumentStatus Статус запрашиваемых инструментов.
     * Возможные значения: 
     * INSTRUMENT_STATUS_UNSPECIFIED	Значение не определено.
     * INSTRUMENT_STATUS_BASE	Базовый список инструментов (по умолчанию). Инструменты доступные для торговли через TINKOFF INVEST API.
     * INSTRUMENT_STATUS_ALL	Список всех инструментов.
     * @return Данные по фондам.
     * @throws ServiceException
     */
    public EtfsResponse Etfs(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getEtfsMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }

    /**
     *
     * @param instrumentStatus Статус запрашиваемых инструментов.
     * Возможные значения: 
     * INSTRUMENT_STATUS_UNSPECIFIED	Значение не определено.
     * INSTRUMENT_STATUS_BASE	Базовый список инструментов (по умолчанию). Инструменты доступные для торговли через TINKOFF INVEST API.
     * INSTRUMENT_STATUS_ALL	Список всех инструментов.
     * @return Данные по фьючерсам.
     * @throws ServiceException
     */
    public FuturesResponse Futures(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getFuturesMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }
    
    /**
     *
     * @param instrumentStatus Статус запрашиваемых инструментов.
     * Возможные значения: 
     * INSTRUMENT_STATUS_UNSPECIFIED	Значение не определено.
     * INSTRUMENT_STATUS_BASE	Базовый список инструментов (по умолчанию). Инструменты доступные для торговли через TINKOFF INVEST API.
     * INSTRUMENT_STATUS_ALL	Список всех инструментов.
     * @return Данные по акциям.
     * @throws ServiceException
     */
    public SharesResponse Shares(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getSharesMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }

    /**
     *
     * @param figi Figi-идентификатор инструмента.
     * @param from Начало запрашиваемого периода в часовом поясе UTC.
     * @param to Окончание запрашиваемого периода в часовом поясе UTC.
     * @return Массив операций начисления купонов.
     * @throws ServiceException
     */
    public GetAccruedInterestsResponse GetAccruedInterests(String figi, com.google.protobuf.Timestamp from, com.google.protobuf.Timestamp to) throws ServiceException {
        var build = GetAccruedInterestsRequest.newBuilder();
        if (figi != null) build.setFigi(figi);
        if (from != null) build.setFrom(from);
        if (to != null) build.setTo(to);
        return CallMethod(
                InstrumentsServiceGrpc.getGetAccruedInterestsMethod(),
                build.build()
        );
    }
    
    
    
}
