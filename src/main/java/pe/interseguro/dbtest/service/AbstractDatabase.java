package pe.interseguro.dbtest.service;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Transaction;
import pe.interseguro.dbtest.exception.ServiceException;
import pe.interseguro.dbtest.util.ResponseCode;

import java.util.function.Function;

public class AbstractDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDatabase.class);

    @Inject
    private PgPool pgPool;

    public <T> Future <T> executeTransaction (Function<SqlConnection, Future<T>> function) {
        return Future.future(promise ->
            pgPool.getConnection(connectionResult -> {
                if (connectionResult.succeeded()) {
                    SqlConnection connection = connectionResult.result();
                    Transaction transaction = connection.begin();
                    function.apply(connection)
                        .onSuccess(voidResult -> {
                            LOGGER.debug("[executeTransaction] se confirma la transaccion");
                            transaction.commit();
                            connection.close();
                        })
                        .onFailure(errorResult -> {
                            LOGGER.debug("[executeTransaction] se cancela la transaccion");
                            transaction.rollback();
                            connection.close();
                        })
                        .onComplete(promise);
                } else {
                    promise.handle(Future.failedFuture(new ServiceException(
                        ResponseCode.DATABASE_CONNECTION_ERROR,
                        "No se pudo crear la conexion de la transaccion a la base de datos",
                        connectionResult.cause()
                    )));
                }
            })
        );
    }

    public <T> Future <T> executeQuery (Function<SqlConnection, Future<T>> function) {
        return Future.future(promise -> {
            pgPool.getConnection(connectionResult -> {
                if (connectionResult.succeeded()) {
                    SqlConnection connection = connectionResult.result();
                    function.apply(connection)
                        .onComplete(result -> {
                            LOGGER.debug("[executeQuery] se cierra la conexion");
                            connection.close();
                        })
                        .onComplete(promise);
                } else {
                    promise.handle(Future.failedFuture(new ServiceException(
                        ResponseCode.DATABASE_CONNECTION_ERROR,
                        "No se pudo crear la coneccion a la base de datos",
                        connectionResult.cause()
                    )));
                }
            });
        });
    }
}
