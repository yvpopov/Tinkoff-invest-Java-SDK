package ru.yvpopov.tinkoffsdk.services.helpers;

public class TinkoffServiceException extends Exception {

    public TinkoffServiceException(String message) {
        super(message);
    }

    public TinkoffServiceException(String message, Throwable cause) {
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
