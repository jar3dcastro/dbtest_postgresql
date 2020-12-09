package pe.interseguro.dbtest.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import pe.interseguro.dbtest.exception.ServiceException;
import pe.interseguro.dbtest.util.HttpCodeResponse;
import pe.interseguro.dbtest.util.ResponseCode;

import java.util.function.Function;

public abstract class AbstractApiVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApiVerticle.class);
    private static final String CONTENT_TYPE = "content-type";
    private static final String APPLICATION_JSON = "application/json";

    protected <T> Handler<AsyncResult<T>> resultHandler(RoutingContext routingContext, Function<T, JsonObject> converter) {
        return asyncResult -> {
            if (asyncResult.succeeded()) {
                T result = asyncResult.result();
                JsonObject resultJson = converter == null || result == null ? null : converter.apply(result);
                LOGGER.debug("[resultHandler] Se proceso la solicitud con exito");
                createResponse(ResponseCode.SUCCESS, resultJson, routingContext);
            } else {
                Throwable throwable = asyncResult.cause();
                LOGGER.error("[resultHandler] Ocurrio un error al procesar la solicitud", throwable);
                if (throwable instanceof ServiceException) {
                    serverErrorResponse((ServiceException) throwable, routingContext);
                } else {
                    serverErrorResponse(routingContext);
                }
            }
        };
    }

    protected void badRequestResponse (String parameter, String cause, RoutingContext routingContext) {
        JsonObject response = new JsonObject()
            .put("parameter", parameter)
            .put("cause", cause);
        createResponse(ResponseCode.BAD_REQUEST_ERROR, response, routingContext);
    }

    private void serverErrorResponse (ServiceException serviceException, RoutingContext routingContext) {
        ResponseCode responseCode = serviceException.getResponseCode();
        JsonObject detail = new JsonObject().put("detail", serviceException.getMessage());
        createResponse(responseCode, detail, routingContext);
    }

    protected void serverErrorResponse (RoutingContext routingContext) {
        createResponse (ResponseCode.UNFORESEEN_ERROR, null, routingContext);
    }

    private void createResponse(ResponseCode responseCode, JsonObject jsonObject, RoutingContext routingContext) {
        JsonObject response = new JsonObject()
            .put("code", responseCode.code)
            .put("message", responseCode.message);
        if (jsonObject != null) {
            response.put("body", jsonObject);
        }
        String responseEncode = response.encodePrettily();
        routingContext.response()
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .setStatusCode(HttpCodeResponse.OK.code)
            .end(responseEncode);
    }
}
