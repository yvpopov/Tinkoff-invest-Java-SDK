package ru.yvpopov.tinkoffsdk.services.child;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import ru.tinkoff.piapi.contract.v1.Bond;
import ru.tinkoff.piapi.contract.v1.Currency;
import ru.tinkoff.piapi.contract.v1.Etf;
import ru.tinkoff.piapi.contract.v1.Future;
import ru.tinkoff.piapi.contract.v1.InstrumentIdType;
import ru.tinkoff.piapi.contract.v1.InstrumentRequest;
import ru.tinkoff.piapi.contract.v1.InstrumentStatus;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.Instruments;
import ru.yvpopov.tinkoffsdk.services.helpers.ServiceException;

/**
 *
 * @author yvpop
 */
public class InstrumentsMod extends Instruments {

    public InstrumentsMod(Communication communication) {
        super(communication);
        this.instrumentslist = new ArrayList<>();
        this.instrumentslist.clear();
        for (TypeInstrument ti: TypeInstrument.values()) 
            this.instrumentslist.add(new ListInstrumentCache(ti));
        this.LastRenewinstrumentslist = Instant.now();
    }

    private ArrayList<ListInstrumentCache> instrumentslist;
    private Instant LastRenewinstrumentslist;

    private class ListInstrumentCache {

        private TypeInstrument typeinstrument;
        private ArrayList<InstrumentCache> instrumentlist;
        private boolean renew;

        public TypeInstrument getTypeinstrument() {
            return this.typeinstrument;
        }

        public ListInstrumentCache(TypeInstrument typeinstrument) {
            this.typeinstrument = typeinstrument;
            this.instrumentlist = new ArrayList<>();
            this.renew = true;
        }

        private void addInstrList() throws ServiceException {
            switch (this.typeinstrument) {
                case Bonds ->
                    Bonds(InstrumentStatus.INSTRUMENT_STATUS_ALL).getInstrumentsList().forEach(e -> instrumentlist.add(new InstrumentCache(e)));
                case Currencys ->
                    Currencys(InstrumentStatus.INSTRUMENT_STATUS_ALL).getInstrumentsList().forEach(e -> instrumentlist.add(new InstrumentCache(e)));
                case Etfs ->
                    Etfs(InstrumentStatus.INSTRUMENT_STATUS_ALL).getInstrumentsList().forEach(e -> instrumentlist.add(new InstrumentCache(e)));
                case Futures ->
                    Futures(InstrumentStatus.INSTRUMENT_STATUS_ALL).getInstrumentsList().forEach(e -> instrumentlist.add(new InstrumentCache(e)));
                case Shares ->
                    Shares(InstrumentStatus.INSTRUMENT_STATUS_ALL).getInstrumentsList().forEach(e -> instrumentlist.add(new InstrumentCache(e)));
                default -> {
                }
            }
        }

        private void RenewInstrumentList() throws ServiceException {
            instrumentlist.clear();
            addInstrList();
            Collections.sort(instrumentlist);
            int n = 0;
            while (n < instrumentlist.size() - 1) {
                if (instrumentlist.get(n).equals(instrumentlist.get(n + 1))) {
                    InstrumentCache ic = instrumentlist.get(n);
                    int i = instrumentlist.indexOf(ic);
                    while (i >= 0) {
                        //System.out.println("remove :" + instrumentlist.get(i).toString());
                        instrumentlist.remove(i);
                        i = instrumentlist.indexOf(ic);
                    }
                } else {
                    n++;
                }
            }
            this.renew = false;
        }

        public void SetRenew() {
            this.renew = true;
        }
        
        public ArrayList<InstrumentCache> getInstrumentlist() {
            if (this.renew) {
                try {
                    RenewInstrumentList();
                } catch (ServiceException ex) {
                    System.err.println(ex);
                }
            }
            return this.instrumentlist;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 89 * hash + Objects.hashCode(this.typeinstrument);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ListInstrumentCache other = (ListInstrumentCache) obj;
            return this.typeinstrument == other.typeinstrument;
        }

    }

    private class InstrumentCache implements Comparable<InstrumentCache> {

        private String ticker;
        private String figi;
        private String class_code;

        public InstrumentCache(String ticker) {
            setTicker(ticker);
        }

        public InstrumentCache(String ticker, String figi, String class_code) {
            this(ticker);
            this.figi = figi;
            this.class_code = class_code;
        }

        public InstrumentCache(Bond instrument) {
            this(instrument.getTicker(), instrument.getFigi(), instrument.getClassCode());
        }

        public InstrumentCache(Etf instrument) {
            this(instrument.getTicker(), instrument.getFigi(), instrument.getClassCode());
        }

        public InstrumentCache(Share instrument) {
            this(instrument.getTicker(), instrument.getFigi(), instrument.getClassCode());
        }

        public InstrumentCache(Currency instrument) {
            this(instrument.getTicker(), instrument.getFigi(), instrument.getClassCode());
        }

        public InstrumentCache(Future instrument) {
            this(instrument.getTicker(), instrument.getFigi(), instrument.getClassCode());
        }

        public String getTicker() {
            return ticker;
        }

        public final void setTicker(String ticker) {
            this.ticker = ticker.toUpperCase();
        }

        public String getFigi() {
            return figi;
        }

        public void setFigi(String figi) {
            this.figi = figi;
        }

        public String getClass_code() {
            return class_code;
        }

        public void setClass_code(String class_code) {
            this.class_code = class_code;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.ticker);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InstrumentCache other = (InstrumentCache) obj;
            return Objects.equals(this.ticker, other.ticker);
        }

        @Override
        public int compareTo(InstrumentCache o) {
            return getTicker().compareTo(o.getTicker());
        }

        @Override
        public String toString() {
            return "InstrumentCache{" + "ticker=" + ticker + ", figi=" + figi + ", class_code=" + class_code + '}';
        }
    }
    
    private void RenewInstrumentList() {
        instrumentslist.forEach(e -> e.SetRenew());
        LastRenewinstrumentslist = Instant.now();
    }

    private ArrayList<InstrumentCache> getInstrumentlist(TypeInstrument typeinstrument) {
        for (ListInstrumentCache ic : this.instrumentslist) {
            if(ic.getTypeinstrument().equals(typeinstrument))
                return ic.getInstrumentlist();
        }
        return new ListInstrumentCache(typeinstrument).getInstrumentlist();
    }
    
    private String GetFigiByTicker(TypeInstrument typeinstrument, String ticker) {
        boolean renew = (LastRenewinstrumentslist.plus(1, java.time.temporal.ChronoUnit.DAYS).compareTo(Instant.now()) < 0);
        return GetFigiByTicker(typeinstrument, ticker, renew);
    }

    private String GetFigiByTicker(TypeInstrument typeinstrument, String ticker, boolean Renew) {
        if (Renew) 
            RenewInstrumentList();
        ArrayList<InstrumentCache> aic = getInstrumentlist(typeinstrument);
        int i = aic.indexOf(new InstrumentCache(ticker));
        return (i >= 0 ? aic.get(i).getFigi() : null);
    }

    @Override
    protected InstrumentRequest instrumentrequest(TypeInstrument typeinstrument, InstrumentIdType id_type, String class_code, String id) {
        if (typeinstrument != null && id_type == InstrumentIdType.INSTRUMENT_ID_TYPE_TICKER && class_code == null) {
            String figi = GetFigiByTicker(typeinstrument, id);
            if (figi != null) {
                id_type = InstrumentIdType.INSTRUMENT_ID_TYPE_FIGI;
                id = figi;
            }
        }
        return super.instrumentrequest(typeinstrument, id_type, class_code, id);
    }

}
