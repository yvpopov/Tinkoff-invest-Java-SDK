package ru.yvpopov.tinkoffsdk;

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
    
    public TinkoffSDK(List<String> tokens, String address) {
        this.address = address;
        this.tokens = tokens;
    }

}
