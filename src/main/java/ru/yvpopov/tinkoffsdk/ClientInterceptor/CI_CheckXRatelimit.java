package ru.yvpopov.tinkoffsdk.ClientInterceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.apache.commons.lang3.time.DateUtils.parseDate;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.Header.HeaderResponseTinkoff;

public class CI_CheckXRatelimit implements ClientInterceptor {

    private final Communication communication;

    private HeaderResponseTinkoff LastHeaderResponse() {
        return communication.getLastInputHeader();
    }

    public CI_CheckXRatelimit(Communication communication) {
        this.communication = communication;

    }
    /**
     *
     * @return Осталось запросов
     */
    private int getIRatelimitRemain() {
        return toInt(LastHeaderResponse().getXRatelimitRemain());
    }
    /**
     *
     * @return Время сброса x-ratelimit-limit
     */
    public Calendar getIDateTimeRatelimitReset() {
        Calendar cal = this.getIDateTimeHeader();
        cal.add(Calendar.SECOND, toInt(LastHeaderResponse().getXRatelimitReset(), -1));
        return cal;
    }

    /**
     *
     * @return Время запроса из заголовка
     */
    public Calendar getIDateTimeHeader() {
        String[] parsePatterns = new String[]{
            "EEE, dd MMM yyyy HH:mm:ss zzz" //Wed, 09 Feb 2022 06:52:57 GMT
        };
        Calendar cal = Calendar.getInstance();
        String date = LastHeaderResponse().getDate();
        if (date == null) {
            return cal;
        }
        try {
            cal.setTime(parseDate(date, Locale.ENGLISH, parsePatterns));
        } catch (ParseException | java.lang.IllegalArgumentException ex) {
            System.err.println(ex.fillInStackTrace().toString());
        }
        return cal;
    }

    /**
     * 
     * @return Признак остатка лимита
     */
    private boolean isILimitRemain() {
        System.out.printf("Debug LimitRemain{%d; %d; %d} \n", getIRatelimitRemain(), getIDateTimeRatelimitReset().getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
        return (getIRatelimitRemain() > 0)
                || (getIDateTimeRatelimitReset().getTimeInMillis() < Calendar.getInstance().getTimeInMillis());
    }

    
    /**
     * Проверяет лимит и при его отсутствии делает паузу до момента сброса лимита
     */
    private void CheckLimit() {
        if (!isILimitRemain()) {
            System.out.printf("Лимит исчерпан до '%s'\n", getIDateTimeRatelimitReset().getTime());
            long pause_ms = (getIDateTimeRatelimitReset().getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
            if (0 <= pause_ms) {
                System.out.printf("Ждем %d мс.\n", pause_ms);
            }
            try {
                Thread.sleep(pause_ms);
            } catch (InterruptedException ex) {
                //
            }
        }
    }

    
    
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                channel.newCall(methodDescriptor, callOptions)) {

            @Override
            public void sendMessage(ReqT message) {
                //System.out.printf("Sending method '%s' message '%s'%n", methodDescriptor.getFullMethodName(),message.toString());
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                /*System.out.println("LastMethod "+LastHeaderResponse().getField("method"));
                System.out.println("CurrentMethod "+methodDescriptor.getFullMethodName());*/
                
                
                //Debug
                //System.out.println(CI_CheckXRatelimit.class.getSimpleName() + "!!");
                //System.out.println(Calendar.getInstance().getTime());
                CheckLimit();

                /*ClientCall.Listener<RespT> listener = new ForwardingClientCallListener<RespT>() {
                    @Override
                    protected Listener<RespT> delegate() {
                        return responseListener;
                    }

                    @Override
                    public void onMessage(RespT message) {
                        System.out.printf("Received message '%s'%n", message.toString());
                        super.onMessage(message);
                    }
                };*/
                super.start(responseListener, headers);
            }
        };
    }

}
