package ru.yvpopov.tinkoffsdk.services.child;

import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.TinkoffSDK;
import ru.yvpopov.tinkoffsdk.services.Accounts;
import ru.yvpopov.tinkoffsdk.services.IAllServices;
import ru.yvpopov.tinkoffsdk.services.Operations;
import ru.yvpopov.tinkoffsdk.services.Orders;
import ru.yvpopov.tinkoffsdk.services.Sandbox;
import ru.yvpopov.tinkoffsdk.services.StopOrders;

/**
 * Модифицированные классы сервисов
 * @author yvpop
 */
public class ServicesWithChild implements IAllServices{
    
    TinkoffSDK sdk;

    public ServicesWithChild(TinkoffSDK sdk) {
        this.sdk = sdk;
    }

    private Communication newCommunication() {
        return sdk.newCommunication();
    }
    
    private InstrumentsChild001 instruments = null;
    
    @Override
    public InstrumentsChild001 getInstruments() {
        if (instruments == null)
            instruments = new InstrumentsChild001(newCommunication());
        return instruments;
    }

    private MarketdataChild003 marketdata = null;

    @Override
    public MarketdataChild003 getMarketdata() {
        if (marketdata == null)
            marketdata = new MarketdataChild003(newCommunication(), getInstruments());
        return marketdata;
    }

    @Override
    public Accounts getAccounts() {
        return sdk.getServices().getAccounts();
    }

    @Override
    public Operations getOperations() {
        return sdk.getServices().getOperations();
    }

    @Override
    public Orders getOrders() {
        return sdk.getServices().getOrders();
    }
    
    @Override
    public StopOrders getStopOrders() {
        return sdk.getServices().getStopOrders();
    }
    
    @Override
    public Sandbox getSandbox() {
        return sdk.getServices().getSandbox();
    }    
}
