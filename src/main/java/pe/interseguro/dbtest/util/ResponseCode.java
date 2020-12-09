package pe.interseguro.dbtest.util;

public enum ResponseCode {

    SUCCESS (0, "La solicitud se ha procesado con exito."),
    UNFORESEEN_ERROR (1, "Error no previsto."),
    CONFIGURATION_SERVICE_ERROR (2, "No se pudo obtener los datos de configuracion"),
    BAD_REQUEST_ERROR (100, "Error en los datos ingresados."),
    PLATE_IS_MOTORCYCLE (101, "No cotizamos para motos por el momento."),
    VALIDATION_ERROR (102, "No tiene permiso para modificar los datos."),
    DATABASE_CONNECTION_ERROR (103, "No se pudo conectar a la base de datos");

    public final int code;
    public final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
