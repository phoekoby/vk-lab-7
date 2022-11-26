package org.example;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.example.config.ProductModule;
import org.example.service.MigrationService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ProductModule());
        injector.getInstance(MigrationService.class).createMigrations();
    }
}