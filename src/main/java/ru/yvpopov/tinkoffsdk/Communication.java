package ru.yvpopov.tinkoffsdk;

import ru.yvpopov.tinkoffsdk.ClientInterceptor.CI_CheckXRatelimit;
import ru.yvpopov.tinkoffsdk.Header.HeaderResponseTinkoff;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import java.util.concurrent.atomic.AtomicReference;
import ru.yvpopov.tinkoffsdk.ClientInterceptor.CI_HeaderAttaching;
import ru.yvpopov.tinkoffsdk.ClientInterceptor.CI_HeadersCapture;

public class Communication {

    private final ManagedChannel channel;

    Communication(String token, String address) {
        this(token,address,true);
    }
    
    Communication(String token, String address, boolean ControlLimit) {
        Metadata header = new Metadata();
        header.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), String.format("Bearer %s", token));
        var pre = ManagedChannelBuilder
                .forTarget(address)
                .useTransportSecurity() //https (ssl/tls)
                .intercept(new CI_HeaderAttaching(header))
                .intercept(new CI_HeadersCapture(InputHeaders, InputTrailers));
        if (ControlLimit) pre.intercept(new CI_CheckXRatelimit(this));
        channel = pre.build();
    }

    private final AtomicReference<Metadata> InputHeaders = new AtomicReference<>();
    private final AtomicReference<Metadata> InputTrailers = new AtomicReference<>();

    private AtomicReference<Metadata> getLastInputMetadata() {
        return (InputHeaders == null || InputHeaders.get() == null ? InputTrailers : InputHeaders);
    }

    public HeaderResponseTinkoff getLastInputHeader() {
        return new HeaderResponseTinkoff(getLastInputMetadata());
    }


    public ManagedChannel getChannel() {
        return this.channel;
    }
}
