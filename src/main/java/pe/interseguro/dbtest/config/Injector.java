package pe.interseguro.dbtest.config;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

public final class Injector {

    private static com.google.inject.Injector injector;

    public static void start (AbstractModule abstractModule) {
        if (injector == null) {
            injector = Guice.createInjector(abstractModule);
        }
    }

    public static void injectMembers (Object object) {
        injector.injectMembers(object);
    }

    public static <T> T getInstance (Class<T> clazz) {
        return injector.getInstance(clazz);
    }
}
