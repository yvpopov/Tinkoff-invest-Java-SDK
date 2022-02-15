package ru.yvpopov.tinkoffsdk.services.child;

import ru.yvpopov.tinkoffsdk.TinkoffSDK;
import ru.yvpopov.tinkoffsdk.services.Accounts;
import ru.yvpopov.tinkoffsdk.services.IAllServices;
import ru.yvpopov.tinkoffsdk.services.ServiceBase;

/**
 * Модифицированные классы сервисов
 * @author yvpop
 */
public class ServicesWithChild implements IAllServices{
    
    TinkoffSDK sdk;

    public ServicesWithChild(TinkoffSDK sdk) {
        this.sdk = sdk;
    }
    
    private InstrumentsChild001 instruments = null;
    private MarketdataChild001 marketdata = null;
    private Accounts accounts = null;
    
    @Override
    public InstrumentsChild001 getInstruments() {
        if (instruments == null)
            instruments = new InstrumentsChild001(sdk.newCommunication());
        return instruments;
    }

    @Override
    public MarketdataChild001 getMarketdata() {
        if (marketdata == null)
            marketdata = new MarketdataChild001(sdk.newCommunication());
        return marketdata;
    }

    @Override
    public Accounts getAccounts() {
        if (accounts == null)
            accounts = new Accounts(sdk.newCommunication());
        return accounts;
    }
    
}
