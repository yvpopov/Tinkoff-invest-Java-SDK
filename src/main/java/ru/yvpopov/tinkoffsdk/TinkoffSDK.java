package ru.yvpopov.tinkoffsdk;

import java.util.Arrays;
import java.util.List;
import ru.yvpopov.tinkoffsdk.services.*;
import ru.yvpopov.tinkoffsdk.services.child.ServicesWithChild;

public class TinkoffSDK implements IAllServices{

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

    private Instruments instruments = null;
    private Accounts accounts = null;
    private Marketdata marketdata = null;
    private Operations operations = null;
    private ServicesWithChild serviceschild;

    /**
     * 
     * @return Последние версии сервисов, включая все модификации
     */
    
    public ServicesWithChild getServicesWithChild() {
        return serviceschild;
    }

    @Override
    public Instruments getInstruments() {
        if (instruments == null)
            instruments = new Instruments(newCommunication());
        return instruments;
    }

    @Override
    public Accounts getAccounts() {
        if (accounts == null)
            accounts = new Accounts(newCommunication());
        return accounts;
    }

    @Override
    public Marketdata getMarketdata() {
        if (marketdata == null)
            marketdata = new Marketdata(newCommunication());
        return marketdata;
    }
    
    @Override
    public Operations getOperations() {
        if (operations == null)
            operations = new Operations(newCommunication());
        return operations;
    }
    
    
    public TinkoffSDK(List<String> tokens, String address) {
        this.address = address;
        this.tokens = tokens;
        this.serviceschild = new ServicesWithChild(this);
    }

}
