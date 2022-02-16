package ru.yvpopov.tinkoffsdk.ClientInterceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.apache.commons.lang3.time.DateUtils.parseDate;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.Header.HeaderResponseTinkoff;

public class CI_CheckXRatelimit extends CI_HeaderAttaching {

    private HeaderResponseTinkoff LastHeaderResponse() {
        return getCommunication().getLastInputHeader();
    }

    private int LimitRemainQueryForNextToken = 1;

    public CI_CheckXRatelimit(Communication communication) {
        super(communication);
        if (communication.getTokens().size() == 1) {
            this.LimitRemainQueryForNextToken = 0;
        }
    }

    protected CI_CheckXRatelimit(Communication communication, int LimitRemainQueryForNextToken) {
        super(communication);
        this.LimitRemainQueryForNextToken = LimitRemainQueryForNextToken;
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
        return (getIRatelimitRemain() > this.LimitRemainQueryForNextToken)
                || (getIDateTimeRatelimitReset().getTimeInMillis() < Calendar.getInstance().getTimeInMillis());
    }

    Integer lastremai = null;

    private boolean NextToken() {
        if (lastremai != null && lastremai > getIRatelimitRemain()) 
            return false;
        if (getCommunication().NextToken()) {
            lastremai = this.LimitRemainQueryForNextToken;
            return true;
        } else return false;
    }

    /**
     * Проверяет лимит и при его отсутствии делает паузу до момента сброса
     * лимита
     */
    private void CheckLimit() {
        if (!isILimitRemain() && !NextToken()) {
            System.out.printf("Лимит исчерпан до '%s'\n", getIDateTimeRatelimitReset().getTime());
            long pause_ms = (getIDateTimeRatelimitReset().getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
            if (0 <= pause_ms) {
                System.out.printf("Ждем %d мс.\n", pause_ms);
            }
            try {
                Thread.sleep(pause_ms);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        return new CheckXRatelimitClientCall<>(channel.newCall(methodDescriptor, callOptions));
    }

    protected final class CheckXRatelimitClientCall<ReqT, RespT>
            extends HeaderAttachingClientCall<ReqT, RespT> {

        // Non private to avoid synthetic class
        CheckXRatelimitClientCall(ClientCall<ReqT, RespT> call) {
            super(call);
        }

        @Override
        public void sendMessage(ReqT message) {
            super.sendMessage(message);
        }

        @Override
        public void start(Listener<RespT> responseListener, Metadata headers) {
            CheckLimit();
            super.start(responseListener, headers);
        }

    }

}
