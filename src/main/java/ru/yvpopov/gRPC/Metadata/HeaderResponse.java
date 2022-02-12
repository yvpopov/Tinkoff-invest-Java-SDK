package ru.yvpopov.gRPC.Metadata;

import io.grpc.Metadata;
import java.util.concurrent.atomic.AtomicReference;

public class HeaderResponse {

    Metadata metadata;

    public HeaderResponse(AtomicReference<Metadata> metadata) {
        this(metadata.get());
    }

    public HeaderResponse(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return (this.metadata == null ? new Metadata() : this.metadata);
    }

    @Override
    public String toString() {
        return getMetadata().toString();
    }

    public String getField(String name) {
        return getMetadata()
                .get(
                        Metadata.Key.of(name, Metadata.ASCII_STRING_MARSHALLER
                        )
                );
    }

    public String getContentType() {
        return getField("content-type");
    }

    public String getDate() {
        return getField("date");
    }

    public String getServer() {
        return getField("server");
    }

}
