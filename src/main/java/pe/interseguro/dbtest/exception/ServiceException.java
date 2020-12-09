package pe.interseguro.dbtest.exception;

import pe.interseguro.dbtest.util.HttpCodeResponse;
import pe.interseguro.dbtest.util.ResponseCode;

public class ServiceException extends io.vertx.serviceproxy.ServiceException {

    private ResponseCode responseCode;

    public ServiceException(ResponseCode responseCode, String message, Throwable throwable) {
        super(HttpCodeResponse.OK.code, message);
        this.responseCode = responseCode;
        setStackTrace(throwable.getStackTrace());
    }

    public ServiceException(ResponseCode responseCode, String message) {
        super(HttpCodeResponse.BAD_REQUEST.code, message);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
