package pe.interseguro.dbtest.service;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import pe.interseguro.dbtest.config.Injector;

import java.time.LocalDateTime;

import pe.interseguro.dbtest.dao.EntityDAO;
import pe.interseguro.dbtest.entity.Entity;

public class EntityServiceImpl extends AbstractDatabase implements EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityServiceImpl.class);

    @Inject
    private EntityDAO entityDAO;

    public EntityServiceImpl(Handler<AsyncResult<EntityService>> readyHandler) {
        Injector.injectMembers(this);
        readyHandler.handle(Future.succeededFuture(this));
    }

    @Override
    public EntityService saveEntity (JsonObject request, Handler<AsyncResult<Void>> resultHandler) {
        executeTransaction(sqlConnection ->
            Future
                .<Void>future(resultPromise -> {
                    Entity entity = new Entity();
                    entity.setId(request.getInteger("id", null));
                    entity.setIntField(request.getInteger("int_field", null));
                    entity.setFloatField(request.getFloat("float_field", null));
                    entity.setStringField(request.getString("string_field", null));
                    entity.setLocalDateTimeField(LocalDateTime.now());
                    entityDAO.saveOrUpdate(entity, sqlConnection, resultPromise);
                })
        )
        .onComplete(resultHandler);
        return this;
    }

    @Override
    public EntityService findAllEntities (Handler<AsyncResult<JsonObject>> resultHandler) {
        executeTransaction(sqlConnection ->
            Future
                .<JsonObject>future(resultPromise -> {
                    entityDAO.findAll(sqlConnection, resultPromise);
                })
        )
        .onComplete(resultHandler);
        return this;
    }

    @Override
    public EntityService findEntityById (Integer id, Handler<AsyncResult<JsonObject>> resultHandler) {
        executeTransaction(sqlConnection ->
            Future
                .<JsonObject>future(resultPromise -> {
                    entityDAO.findById(id, sqlConnection, resultPromise);
                })
        )
        .onComplete(resultHandler);
        return this;
    }

}
