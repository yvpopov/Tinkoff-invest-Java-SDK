package ru.yvpopov.tinkoffsdk;

import java.util.Arrays;
import java.util.List;
import ru.yvpopov.tinkoffsdk.services.*;
import ru.yvpopov.tinkoffsdk.services.child.ServicesWithChild;

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

    private ServicesWithChild serviceschild;

    private Services services;

    /**
     * 
     * @return Исходные версии сервисов, без модификаций 
     */
    public Services getServices() {
        return services;
    }

    /**
     * 
     * @return Последние версии сервисов, включая все модификации
     */
    public ServicesWithChild getServicesWithChild() {
        return serviceschild;
    }

    public TinkoffSDK(List<String> tokens, String address) {
        this.address = address;
        this.tokens = tokens;
        this.services = new Services(this);
        this.serviceschild = new ServicesWithChild(this);
    }

}
