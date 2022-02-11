package ru.yvpopov.tinkoffsdk.ClientInterceptor;

import static com.google.common.base.Preconditions.checkNotNull;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import java.util.concurrent.atomic.AtomicReference;

public final class CI_HeadersCapture implements ClientInterceptor {

    final AtomicReference<Metadata> headersCapture;
    final AtomicReference<Metadata> trailersCapture;

    // Non private to avoid synthetic class
    public CI_HeadersCapture(
            AtomicReference<Metadata> headersCapture, AtomicReference<Metadata> trailersCapture) {
        this.headersCapture = checkNotNull(headersCapture, "headersCapture");
        this.trailersCapture = checkNotNull(trailersCapture, "trailersCapture");
    }

    MethodDescriptor methodDescriptor;
    
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        methodDescriptor = method;
        return new MetadataCapturingClientCall<>(next.newCall(method, callOptions));
    }

    private final class MetadataCapturingClientCall<ReqT, RespT>
            extends ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> {

        // Non private to avoid synthetic class
        MetadataCapturingClientCall(ClientCall<ReqT, RespT> call) {
            super(call);
        }

        @Override
        public void start(ClientCall.Listener<RespT> responseListener, Metadata headers) {
            headersCapture.set(null);
            trailersCapture.set(null);
            super.start(new MetadataCapturingClientCallListener(responseListener), headers);
        }

        private final class MetadataCapturingClientCallListener
                extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {

            MetadataCapturingClientCallListener(ClientCall.Listener<RespT> responseListener) {
                super(responseListener);
            }

            @Override
            public void onHeaders(Metadata headers) {
                headers.put(Metadata.Key.of("method", Metadata.ASCII_STRING_MARSHALLER), methodDescriptor.getFullMethodName());
                headersCapture.set(headers);
                super.onHeaders(headers);
            }

            @Override
            public void onClose(Status status, Metadata trailers) {
                trailers.put(Metadata.Key.of("method", Metadata.ASCII_STRING_MARSHALLER), methodDescriptor.getFullMethodName());
                trailersCapture.set(trailers);
                super.onClose(status, trailers);
            }
        }
    }
}
