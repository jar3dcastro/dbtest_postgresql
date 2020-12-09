package pe.interseguro.dbtest.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@ProxyGen
@VertxGen
public interface EntityService {

    String ADDRESS = "EntityServiceAddress";

    @Fluent
    EntityService saveEntity (JsonObject request, Handler<AsyncResult<Void>> resultHandler);
    @Fluent
    EntityService findAllEntities (Handler<AsyncResult<JsonObject>> resultHandler);
    @Fluent
    EntityService findEntityById (Integer id, Handler<AsyncResult<JsonObject>> resultHandler);

    @GenIgnore
    static EntityService create(Handler<AsyncResult<EntityService>> readyHandler) {
        return new EntityServiceImpl(readyHandler);
    }

    @GenIgnore
    static EntityService createProxy(Vertx vertx, String address) {
        return new EntityServiceVertxEBProxy(vertx, address);
    }

}
