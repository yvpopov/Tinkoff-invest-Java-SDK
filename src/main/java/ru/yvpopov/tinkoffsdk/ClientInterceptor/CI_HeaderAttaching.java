package ru.yvpopov.tinkoffsdk.ClientInterceptor;

import static com.google.common.base.Preconditions.checkNotNull;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;


public final class CI_HeaderAttaching implements ClientInterceptor {

    private final Metadata extraHeaders;

    public CI_HeaderAttaching(Metadata extraHeaders) {
        this.extraHeaders = checkNotNull(extraHeaders, "extraHeaders");
        this.extraHeaders.put(Metadata.Key.of("x-app-name", Metadata.ASCII_STRING_MARSHALLER), "yvpopov.Tinkoff-invest-Java-SDK");
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
        MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
      return new HeaderAttachingClientCall<>(next.newCall(method, callOptions));
    }

    private final class HeaderAttachingClientCall<ReqT, RespT>
        extends ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> {

      // Non private to avoid synthetic class
      HeaderAttachingClientCall(ClientCall<ReqT, RespT> call) {
        super(call);
      }

      @Override
      public void start(ClientCall.Listener<RespT> responseListener, Metadata headers) {
        headers.merge(extraHeaders);
        super.start(responseListener, headers);
      }
    }
  }