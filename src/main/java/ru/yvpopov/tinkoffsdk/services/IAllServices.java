package ru.yvpopov.tinkoffsdk.services;

public interface IAllServices {
    public ServiceBase getAccounts();
    public ServiceBase getInstruments();
    public ServiceBase getMarketdata();
    public ServiceBase getOperations();
}
