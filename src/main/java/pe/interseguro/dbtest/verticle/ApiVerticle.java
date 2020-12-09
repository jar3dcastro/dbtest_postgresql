package pe.interseguro.dbtest.verticle;

import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.api.validation.ValidationException;
import pe.interseguro.dbtest.config.Injector;

import pe.interseguro.dbtest.service.EntityService;

public class ApiVerticle extends AbstractApiVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiVerticle.class);
    private static final String USER = "CLIENT";

    @Inject
    private EntityService entityService;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Injector.injectMembers(this);
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/api/v1/entity/save").handler(this::saveEntity);
        router.get("/api/v1/entity/findAll").handler(this::findAllEntities);
        router.get("/api/v1/entity/findById/:id").handler(this::findEntityById);

        router.errorHandler(500, routingContext -> {
            System.err.println("Handling failure");
            Throwable failure = routingContext.failure();
            if (failure != null) {
                failure.printStackTrace();
            }
        });

        Integer httpPort = config()
            .getJsonObject("verticle")
            .getJsonObject("api")
            .getJsonObject("v1")
            .getInteger("port");

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(httpPort, result -> {
                if (result.succeeded()) {
                    startPromise.complete();
                } else {
                    startPromise.fail(result.cause());
                }
            });

    }

    private void saveEntity (RoutingContext routingContext) {
        JsonObject request = routingContext.getBodyAsJson();
        entityService.saveEntity(request, resultHandler(routingContext, null));
    }

    private void findAllEntities (RoutingContext routingContext) {
        entityService.findAllEntities(resultHandler(routingContext, response -> response));
    }

    private void findEntityById (RoutingContext routingContext) {
        String id = routingContext.pathParam("id");
        entityService.findEntityById(Integer.parseInt(id), resultHandler(routingContext, response -> response));
    }

}
