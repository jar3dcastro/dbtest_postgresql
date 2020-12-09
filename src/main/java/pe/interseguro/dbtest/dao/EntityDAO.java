package pe.interseguro.dbtest.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.sqlclient.*;
import pe.interseguro.dbtest.util.SqlQuery;
import pe.interseguro.dbtest.util.CustomHelper;

import java.util.Optional;

import pe.interseguro.dbtest.entity.Entity;

public class EntityDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(EntityDAO.class);

    public void saveOrUpdate (Entity entity, SqlConnection sqlConnection, Handler<AsyncResult<Void>> resultHandler) {
        Future
            .<RowSet<Row>>future(rowSetPromise -> {
                if (entity.getId() == null) {
                    String query = SqlQuery.query("entity", "insert");
                    sqlConnection
                        .preparedQuery(query)
                        .execute(Tuple.of(
                            entity.getIntField(),
                            entity.getFloatField(),
                            entity.getStringField(),
                            entity.getLocalDateTimeField()
                        ), rowSetPromise);
                } else {
                    String query = SqlQuery.query("entity", "update");
                    sqlConnection
                        .preparedQuery(query)
                        .execute(Tuple.of(
                            entity.getIntField(),
                            entity.getFloatField(),
                            entity.getStringField(),
                            entity.getLocalDateTimeField(),
                            entity.getId()
                        ), rowSetPromise);
                }
            })
            .map((Void)null)
            .onComplete(resultHandler);
    }

    public void findAll (SqlConnection sqlConnection, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future
            .<RowSet<Row>>future(rowSetPromise -> {
                String query = SqlQuery.query("entity", "findAll");
                sqlConnection
                    .preparedQuery(query)
                    .execute(rowSetPromise);
            })
            .map(this::createEntityList)
            .onComplete(resultHandler);
    }

    public void findById (Integer id, SqlConnection sqlConnection, Handler<AsyncResult<JsonObject>> resultHandler) {
        Future
            .<RowSet<Row>>future(rowSetPromise -> {
                String query = SqlQuery.query("entity", "findById");
                sqlConnection
                    .preparedQuery(query)
                    .execute(Tuple.of(id), rowSetPromise);
            })
            .map(this::createEntity)
            .onComplete(resultHandler);
    }

    private JsonObject createEntityList (RowSet<Row> rowRowSet) {
        RowIterator<Row> rowIterator = rowRowSet.iterator();
        JsonArray entityList = new JsonArray();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            JsonObject entity = createEntity(row);
            entityList.add(entity);
        }
        JsonObject responseBody = new JsonObject()
            .put("list", entityList);
        return responseBody;
    }

    private JsonObject createEntity (RowSet<Row> rowRowSet) {
        RowIterator<Row> rowIterator = rowRowSet.iterator();
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            return createEntity(row);
        } else {
            return null;
        }
    }

    private JsonObject createEntity (Row row) {
        JsonObject entity = new JsonObject()
            .put("id", row.getInteger("id"))
            .put("int_field", row.getInteger("int_field"))
            .put("float_field", row.getFloat("float_field"))
            .put("string_field", row.getString("string_field"))
            .put("datetime_field", CustomHelper.dateTime2string(row.getLocalDateTime("datetime_field")));
        return entity;
    }

}
