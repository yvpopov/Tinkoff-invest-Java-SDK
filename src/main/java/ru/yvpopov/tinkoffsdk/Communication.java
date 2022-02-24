package ru.yvpopov.tinkoffsdk;

import ru.yvpopov.tinkoffsdk.ClientInterceptor.CI_CheckXRatelimit;
import ru.yvpopov.tinkoffsdk.Header.HeaderResponseTinkoff;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import ru.yvpopov.tinkoffsdk.ClientInterceptor.CI_HeaderAttaching;
import ru.yvpopov.tinkoffsdk.ClientInterceptor.CI_HeadersCapture;

public class Communication {

    private final ManagedChannel channel;

    private final List<String> tokens;

    private int KeyNo = 0;

    public boolean NextToken() {
        int keyno = KeyNo;
        KeyNo++;
        if (this.tokens.size() == KeyNo) {
            KeyNo = 0;
        }
        return keyno!=KeyNo;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public String getToken() {
        return String.format("Bearer %s", this.tokens.get(KeyNo));
    }

    Communication(List<String> tokens, String address, boolean ControlLimit) {
        this.tokens = tokens;
        ManagedChannelBuilder pre = ManagedChannelBuilder
                .forTarget(address)
                .useTransportSecurity() //https (ssl/tls)
                .intercept(new CI_HeadersCapture(InputHeaders, InputTrailers));
        if (ControlLimit) {
            pre.intercept(new CI_CheckXRatelimit(this));
        } else {
            pre.intercept(new CI_HeaderAttaching(this));
        }
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
