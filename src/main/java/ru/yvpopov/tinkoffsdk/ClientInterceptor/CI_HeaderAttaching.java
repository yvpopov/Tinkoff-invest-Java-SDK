package ru.yvpopov.tinkoffsdk.ClientInterceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.MethodDescriptor;
import ru.yvpopov.tinkoffsdk.Communication;

public class CI_HeaderAttaching implements ClientInterceptor {

    private final Metadata extraHeaders;

    private CI_HeaderAttaching() {
        this.extraHeaders = new Metadata();
        this.extraHeaders.put(Metadata.Key.of("x-app-name", Metadata.ASCII_STRING_MARSHALLER), "yvpopov.Tinkoff-invest-Java-SDK");
        //this.tokens = new ArrayList<>();
    }

    private Communication communication;

    public CI_HeaderAttaching(Communication communication) {
        this();
        this.communication = communication;
    }

    protected Communication getCommunication() {
        return this.communication;
    }

    protected String getToken() {
        return getCommunication().getToken();
    }

    protected Metadata getMetaDate() {
        String maintoken = getToken();
        Key key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
        Object currentmeta = this.extraHeaders.get(key);
        String currenttoken = (currentmeta == null ? null : currentmeta.toString());
        if (!maintoken.equals(currenttoken)) {
            this.extraHeaders.removeAll(key);
            this.extraHeaders.put(key, maintoken);
        }
        return this.extraHeaders;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new HeaderAttachingClientCall<>(next.newCall(method, callOptions));
    }

    protected class HeaderAttachingClientCall<ReqT, RespT>
            extends ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> {

        // Non private to avoid synthetic class
        HeaderAttachingClientCall(ClientCall<ReqT, RespT> call) {
            super(call);
        }

        @Override
        public void start(ClientCall.Listener<RespT> responseListener, Metadata headers) {
            headers.merge(getMetaDate());
            super.start(responseListener, headers);
        }
    }
}
