package pe.interseguro.dbtest.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class SqlQuery {

    private static Map<String, Map<String, String>> queries;

    public static void load () {
        Yaml yaml = new Yaml();
        InputStream inputStream = SqlQuery.class.getResourceAsStream("/db-queries.yaml");
        queries = yaml.load(inputStream);
    }

    public static String query (String entity, String queryName) {
        return queries.get(entity).get(queryName);
    }
}
