package ru.yvpopov.tinkoffsdk.services;

import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;
import ru.yvpopov.tinkoffsdk.services.helpers.Service;
import ru.tinkoff.piapi.contract.v1.*;
import ru.yvpopov.tinkoffsdk.Communication;

public class Instruments extends Service {

    public Instruments(Communication communication) {
        //super(communication, ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.newBlockingStub(communication.getChannel()));
        super(communication, ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.class);
    }

    public TradingSchedulesResponse TradingSchedules(String Exchange, com.google.protobuf.Timestamp from, com.google.protobuf.Timestamp to) throws ServiceException {
        var build = TradingSchedulesRequest.newBuilder();
        if (Exchange != null) {
            build.setExchange(Exchange);
        }
        if (from != null) {
            build.setFrom(from);
        }
        if (to != null) {
            build.setTo(to);
        }
        return CallMethod(
                InstrumentsServiceGrpc.getTradingSchedulesMethod(),
                build.build()
        );
    }

    private InstrumentRequest instrumentrequest(InstrumentIdType id_type, String class_code, String id) {
        var build = InstrumentRequest.newBuilder();
        if (id_type == null) {
            id_type = InstrumentIdType.INSTRUMENT_ID_UNSPECIFIED;
        }
        build.setIdType(id_type);
        if (class_code != null) {
            build.setClassCode(class_code);
        }
        if (id != null) {
            build.setClassCode(id);
        }
        return build.build();
    }

    public BondResponse BondBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getBondByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    public CurrencyResponse CurrencyBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getCurrencyByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    public EtfResponse EtfBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getEtfByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    public FutureResponse FutureBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getFutureByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    public ShareResponse ShareBy(InstrumentIdType id_type, String class_code, String id) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getShareByMethod(),
                instrumentrequest(id_type, class_code, id)
        );
    }

    private InstrumentsRequest instrumentsrequest(InstrumentStatus instrumentStatus) {
        if (instrumentStatus == null) 
            instrumentStatus = InstrumentStatus.INSTRUMENT_STATUS_UNSPECIFIED;
        return InstrumentsRequest.newBuilder().setInstrumentStatus(instrumentStatus).build();
    }
    
    public BondsResponse Bonds(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getBondsMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }

    public CurrenciesResponse Currencys(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getCurrenciesMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }

    public EtfsResponse Etfs(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getEtfsMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }

    public FuturesResponse FutureBy(InstrumentStatus instrumentStatus) throws ServiceException {
        return CallMethod(
                InstrumentsServiceGrpc.getFuturesMethod(),
                instrumentsrequest(instrumentStatus)
        );
    }
}
