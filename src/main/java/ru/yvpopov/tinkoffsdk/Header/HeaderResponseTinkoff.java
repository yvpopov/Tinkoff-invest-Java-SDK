package ru.yvpopov.tinkoffsdk.Header;

import io.grpc.Metadata;
import java.util.concurrent.atomic.AtomicReference;
import ru.yvpopov.gRPC.Metadata.HeaderResponse;

/**
 *
 * @author yvpop
 */
public class HeaderResponseTinkoff extends HeaderResponse{
    
    public HeaderResponseTinkoff(AtomicReference<Metadata> metadata) {
        super(metadata);
    }

    public HeaderResponseTinkoff(Metadata metadata) {
        super(metadata);
    }
    
    public String getXTrackingId() {
        return getField("x-tracking-id");
    }

    public String getMessage() {
        return getField("message");
    }
    
    public String getXEnvoyUpstreamServiceTime() {
        return getField("x-envoy-upstream-service-time");
    }

    /**
     *
     * @return Лимит запросов
     */
    public String getXRatelimitLimit() {
        return getField("x-ratelimit-limit");
    }

    /**
     *
     * @return Осталось запросов
     */
    public String getXRatelimitRemain() {
        return getField("x-ratelimit-remaining");
    }
    
    /**
     *
     * @return Секунд до сброса x-ratelimit-limit
     */
    public String getXRatelimitReset() {
        return getField("x-ratelimit-reset");
    }
    
    /**
     * 
     * @return Метод запроса
     */
    public String getMethod() {
        return getField("method");
    }
 
}
