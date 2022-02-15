package ru.yvpopov.tinkoffsdk;

import ru.yvpopov.tinkoffsdk.services.child.InstrumentsMod001;
import java.util.Arrays;
import java.util.List;
import ru.yvpopov.tinkoffsdk.services.*;

public class TinkoffSDK {

    private List<String> tokens;
    private String address;

    public TinkoffSDK(String token) {
        this(Arrays.asList(token), "invest-public-api.tinkoff.ru:443");
    }

    public TinkoffSDK(List<String> tokens) {
        this(tokens, "invest-public-api.tinkoff.ru:443");
    }
    
    boolean ControlLimit = true;

    public TinkoffSDK setControlLimit(boolean ControlLimit) {
        this.ControlLimit = ControlLimit;
        return this;
    }
    
    public Communication newCommunication() {
        return new Communication(this.tokens, this.address, ControlLimit);
    }

    private InstrumentsMod001 instruments = null;
    private Accounts accounts = null;
    private Marketdata marketdata = null;

    public InstrumentsMod001 getInstruments() {
        if (instruments == null)
            instruments = new InstrumentsMod001(newCommunication());
        return instruments;
    }

    public Accounts getAccounts() {
        if (accounts == null)
            accounts = new Accounts(newCommunication());
        return accounts;
    }

    public Marketdata getMarketdata() {
        if (marketdata == null)
            marketdata = new Marketdata(newCommunication());
        return marketdata;
    }
    
    public TinkoffSDK(List<String> tokens, String address) {
        this.address = address;
        this.tokens = tokens;
    }

}
