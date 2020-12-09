package pe.interseguro.dbtest.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

import pe.interseguro.dbtest.service.EntityService;

public class EntityVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Future
            .future(EntityService::create)
            .onSuccess(entityService -> {
                new ServiceBinder(vertx)
                    .setAddress(EntityService.ADDRESS)
                    .register(EntityService.class, entityService);
                LOGGER.info("[start] Se ha cargado el verticle {}", getClass());
            })
            .map((Void)null)
            .onComplete(startPromise);
    }

}
