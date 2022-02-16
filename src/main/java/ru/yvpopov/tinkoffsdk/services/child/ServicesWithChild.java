package ru.yvpopov.tinkoffsdk.services.child;

import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.TinkoffSDK;
import ru.yvpopov.tinkoffsdk.services.Accounts;
import ru.yvpopov.tinkoffsdk.services.IAllServices;
import ru.yvpopov.tinkoffsdk.services.Operations;
import ru.yvpopov.tinkoffsdk.services.ServiceBase;

/**
 * Модифицированные классы сервисов
 * @author yvpop
 */
public class ServicesWithChild implements IAllServices{
    
    TinkoffSDK sdk;

    private InstrumentsChild001 instruments = null;
    private MarketdataChild001 marketdata = null;
    
    public ServicesWithChild(TinkoffSDK sdk) {
        this.sdk = sdk;
    }

    private Communication newCommunication() {
        return sdk.newCommunication();
    }
    
    
    @Override
    public InstrumentsChild001 getInstruments() {
        if (instruments == null)
            instruments = new InstrumentsChild001(newCommunication());
        return instruments;
    }

    @Override
    public MarketdataChild001 getMarketdata() {
        if (marketdata == null)
            marketdata = new MarketdataChild001(newCommunication());
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
    
}
