package ru.yvpopov.tinkoffsdk;

import ru.yvpopov.tinkoffsdk.services.*;

public class TinkoffSDK {

    private String token;
    private String address;

    public TinkoffSDK(String token) {
        this(token, "invest-public-api.tinkoff.ru:443");
    }
    
    boolean ControlLimit = true;

    public TinkoffSDK setControlLimit(boolean ControlLimit) {
        this.ControlLimit = ControlLimit;
        return this;
    }
    
    public Communication newCommunication() {
        return new Communication(this.token, this.address, ControlLimit);
    }

    private InstrumentsMod instruments = null;
    private Accounts accounts = null;

    public InstrumentsMod getInstruments() {
        if (instruments == null)
            instruments = new InstrumentsMod(newCommunication());
        return instruments;
    }

    public Accounts getAccounts() {
        if (accounts == null)
            accounts = new Accounts(newCommunication());
        return accounts;
    }
    
    public TinkoffSDK(String token, String address) {
        this.address = address;
        this.token = token;
    }

}
