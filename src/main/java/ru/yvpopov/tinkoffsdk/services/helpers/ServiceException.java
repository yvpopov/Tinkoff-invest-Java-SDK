package ru.yvpopov.tinkoffsdk.services.helpers;

public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getMessage());
        sb.append("\n");
        sb.append("ServiceException: ");
        sb.append("{");
        if (getCause() != null) 
            sb.append(getCause().toString());
        sb.append("}");
        return sb.toString();
    }
    
    
    
    
}
