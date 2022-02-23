package ru.yvpopov.tinkoffsdk.services;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import java.lang.reflect.Method;
import io.grpc.stub.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.yvpopov.tinkoffsdk.Communication;
import ru.yvpopov.tinkoffsdk.services.helpers.TinkoffServiceException;

public class ServiceBase {
    
    private static final Logger LOG = Logger.getLogger(ServiceBase.class.getName());

    private final Communication communication;
    private io.grpc.stub.AbstractBlockingStub stub = null;

    public Communication getCommunication() {
        return communication;
    }

    private Channel getChannel() {
        return (stub == null || stub.getChannel() == null ? getCommunication().getChannel() : stub.getChannel());
    }

    private CallOptions getCallOptions() {
        return (stub == null || stub.getCallOptions() == null ? CallOptions.DEFAULT : stub.getCallOptions());
    }

    protected ServiceBase(Communication communication, Class ServiceGrpcClass) {
        this.communication = communication;
        try {
            Method method = ServiceGrpcClass.getMethod("newBlockingStub", io.grpc.Channel.class);
            this.stub = (AbstractBlockingStub) method.invoke(ServiceGrpcClass, this.communication.getChannel());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, String.format("ERROR: AbstractBlockingStub from '%s'.newBlockingStub(Channel) not created\nMessage: %s\nStackTrace: \n%s\n", ServiceGrpcClass.toString(), ex.getMessage(), ex.fillInStackTrace().toString()));
        }
    }

    /**
     *
     * @param <ReqT>
     * @param <RespT>
     * @param method Вызываемый метод
     * @param req 
     * @return 
     * @throws TinkoffServiceException
     */
    protected <ReqT, RespT> RespT CallMethod(MethodDescriptor<ReqT, RespT> method, ReqT req) throws TinkoffServiceException {
        try {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), method, getCallOptions(), req);
        } catch (Exception ex) {
            throw new TinkoffServiceException(getCommunication().getLastInputHeader().getMessage(), ex.fillInStackTrace());
        }
    }

}
