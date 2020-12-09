package pe.interseguro.dbtest.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import pe.interseguro.dbtest.config.DefaultModule;
import pe.interseguro.dbtest.config.Injector;
import pe.interseguro.dbtest.util.SqlQuery;

import java.util.Arrays;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> promise) throws Exception {
        LOGGER.info("[start] iniciando la carga de Verticles para el sistema");
        SqlQuery.load();
        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.getConfig(json -> {
            JsonObject config = json.result();
            Injector.start(new DefaultModule(config, vertx));
            deployVerticles(json.result(), promise);
        });
    }

    private void deployVerticles (JsonObject config, Promise<Void> promise) {
        JsonObject verticleInstances = config.getJsonObject("verticle");
        int apiInstances = verticleInstances.getJsonObject("api").getInteger("instances");
        int entityInstances = verticleInstances.getJsonObject("entity").getInteger("instances");
        LOGGER.debug("[deployVerticles] instancias - apiInstances: {}, entityInstances: {}", apiInstances, entityInstances);
        CompositeFuture
            .join(Arrays.asList(
                Future.<String>future(verticlePromise ->
                    vertx.deployVerticle(
                        ApiVerticle.class.getName(),
                        new DeploymentOptions()
                            .setInstances(apiInstances)
                            .setConfig(config),
                        verticlePromise
                    )
                ),
                Future.<String>future(verticlePromise ->
                    vertx.deployVerticle(
                        EntityVerticle.class.getName(),
                        new DeploymentOptions()
                            .setInstances(entityInstances)
                            .setConfig(config),
                        verticlePromise
                    )
                )
            ))
            .map((Void)null)
            .onFailure(throwable -> {
                LOGGER.error("[deployVerticles] error al desplegar los Verticles", throwable);
            })
            .onComplete(promise);
    }

}
