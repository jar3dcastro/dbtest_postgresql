package pe.interseguro.dbtest.util;

public enum HttpCodeResponse {

    OK (200),
    BAD_REQUEST (400),
    INTERNAL_SERVER_ERROR (500);

    public final int code;

    HttpCodeResponse(int code) {
        this.code = code;
    }
}
