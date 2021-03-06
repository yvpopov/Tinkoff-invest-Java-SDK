package ru.yvpopov.tinkoffsdk.services;

import javax.annotation.Nonnull;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.tinkoff.piapi.contract.v1.*;
import ru.yvpopov.tinkoffsdk.services.helpers.TinkoffServiceException;

public class Operations extends ServiceBase {

    public Operations(@Nonnull final Communication communication) {
        super(communication, ru.tinkoff.piapi.contract.v1.OperationsServiceGrpc.class);
    }

    /**
     *
     * @param account_id Идентификатор счета клиента
     * @param from Начало периода (по UTC)
     * @param to Окончание периода (по UTC)
     * @param operationstate Статус запрашиваемых операций
     * @param figi Figi-идентификатор инструмента для фильтрации
     * @return Список операций по счету
     * @throws TinkoffServiceException
     */
    public OperationsResponse GetOperations(@Nonnull final String account_id,
            @Nonnull final com.google.protobuf.Timestamp from,
            @Nonnull final com.google.protobuf.Timestamp to,
            OperationState operationstate,
            String figi) throws TinkoffServiceException {
        OperationsRequest.Builder build = OperationsRequest.newBuilder();
        build.setAccountId(account_id);
        build.setFrom(from);
        build.setTo(to);
        if (operationstate != null) {
            build.setState(operationstate);
        }
        if (figi != null) {
            build.setFigi(figi);
        }
        return CallMethod(
                OperationsServiceGrpc.getGetOperationsMethod(),
                build.build()
        );
    }

    /**
     *
     * @param account_id Идентификатор счета клиента
     * @return Текущий портфель по счёту
     * @throws TinkoffServiceException
     */
    public PortfolioResponse GetPortfolio(@Nonnull final String account_id) throws TinkoffServiceException {
        PortfolioRequest.Builder build = PortfolioRequest.newBuilder();
        build.setAccountId(account_id);
        return CallMethod(
                OperationsServiceGrpc.getGetPortfolioMethod(),
                build.build()
        );
    }

    /**
     *
     * @param account_id Идентификатор счета клиента
     * @return Список позиций по счёту
     * @throws TinkoffServiceException
     */
    public PositionsResponse GetPositions(@Nonnull final String account_id) throws TinkoffServiceException {
        PositionsRequest.Builder build = PositionsRequest.newBuilder();
        build.setAccountId(account_id);
        return CallMethod(
                OperationsServiceGrpc.getGetPositionsMethod(),
                build.build()
        );
    }
    
    /**
     *
     * @param account_id Идентификатор счета клиента
     * @return Доступный для вывода остаток
     * @throws TinkoffServiceException
     */
    public WithdrawLimitsResponse GetWithdrawLimits(@Nonnull final String account_id) throws TinkoffServiceException {
        WithdrawLimitsRequest.Builder build = WithdrawLimitsRequest.newBuilder();
        build.setAccountId(account_id);
        return CallMethod(
                OperationsServiceGrpc.getGetWithdrawLimitsMethod(),
                build.build()
        );
    }
    
    /**
     *
     * @param account_id Идентификатор счёта клиента
     * @param from Начало периода в часовом поясе UTC.
     * @param to Окончание периода в часовом поясе UTC.
     * @param task_id Идентификатор задачи формирования брокерского отчёта
     * @param page Номер страницы отчета (начинается с 1), значение по умолчанию: 0
     * @return Брокерский отчет
     * @throws TinkoffServiceException
     */
    public BrokerReportResponse GetBrokerReport(
            @Nonnull final String account_id, 
            com.google.protobuf.Timestamp from,
            com.google.protobuf.Timestamp to,
            @Nonnull final String task_id,
            int page
    ) throws TinkoffServiceException {
        GenerateBrokerReportRequest.Builder build = GenerateBrokerReportRequest.newBuilder();
        build.setAccountId(account_id);
        if (from != null) 
            build.setFrom(from);
        if (to != null) 
            build.setFrom(to);
        GetBrokerReportRequest.Builder build1 = GetBrokerReportRequest.newBuilder();
        build1.setTaskId(task_id);
        build1.setPage(page);
        return GetBrokerReport(build.build(), build1.build());
    }
    
    protected BrokerReportResponse GetBrokerReport(
            @Nonnull final GenerateBrokerReportRequest generate_broker_report_request,
            @Nonnull final  GetBrokerReportRequest get_broker_report_request) throws TinkoffServiceException {
        BrokerReportRequest.Builder build = BrokerReportRequest.newBuilder();
        build.setGenerateBrokerReportRequest(generate_broker_report_request);
        build.setGetBrokerReportRequest(get_broker_report_request);
        return CallMethod(
                OperationsServiceGrpc.getGetBrokerReportMethod(),
                build.build()
        );
    }
}
