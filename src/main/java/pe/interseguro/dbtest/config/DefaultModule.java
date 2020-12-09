package pe.interseguro.dbtest.config;

import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

import pe.interseguro.dbtest.dao.EntityDAO;
import pe.interseguro.dbtest.service.EntityService;

public class DefaultModule extends AbstractModule {

    private Vertx vertx;
    private JsonObject config;

    public DefaultModule(JsonObject config, Vertx vertx) {
        this.config = config;
        this.vertx = vertx;
    }

    @Override
    protected void configure() {
        bind(PgPool.class).toInstance(createPgPool());

        bind(EntityDAO.class).toInstance(new EntityDAO());

        bind(EntityService.class).toInstance(EntityService.createProxy(vertx, EntityService.ADDRESS));

        bind(WebClient.class).toInstance(WebClient.create(vertx));
    }

    private PgPool createPgPool () {
        JsonObject databaseConfig = config.getJsonObject("database");
        PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(databaseConfig.getInteger("port"))
            .setHost(databaseConfig.getString("host"))
            .setDatabase(databaseConfig.getString("database"))
            .setUser(databaseConfig.getString("username"))
            .setPassword(databaseConfig.getString("password"));

        PoolOptions poolOptions = new PoolOptions().setMaxSize(databaseConfig.getInteger("maxPoolSize"));

        return PgPool.pool(vertx, connectOptions, poolOptions);
    }
}
