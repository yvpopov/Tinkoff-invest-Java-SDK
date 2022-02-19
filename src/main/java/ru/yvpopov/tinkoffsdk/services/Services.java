/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.yvpopov.tinkoffsdk.services;

import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.TinkoffSDK;

/**
 *
 * @author yvpop
 */
public class Services implements IAllServices {
    
    TinkoffSDK sdk;
    
    private Instruments instruments = null;
    private Accounts accounts = null;
    private Marketdata marketdata = null;
    private Operations operations = null;
    private Orders orders = null;
    private StopOrders stoporders = null;
    private Sandbox sandbox = null;
    

    public Services(TinkoffSDK sdk) {
        this.sdk = sdk;
    }
    
    private Communication newCommunication() {
        return sdk.newCommunication();
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

    @Override
    public Orders getOrders() {
        if (orders == null)
            orders = new Orders(newCommunication());
        return orders;
    }

    @Override
    public StopOrders getStopOrders() {
        if (stoporders == null)
            stoporders = new StopOrders(newCommunication());
        return stoporders;
    }

    @Override
    public Sandbox getSandbox() {
        if (sandbox == null)
            sandbox = new Sandbox(newCommunication());
        return sandbox;
    }
    
}
