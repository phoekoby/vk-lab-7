package org.example.config;

import com.google.inject.AbstractModule;
import org.example.service.MigrationService;

import static org.example.config.DbConstants.*;

public class ProductModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MigrationService.class);
        bind(DBCredentials.class).toInstance(new DBCredentials(CONNECTION, DB_NAME, USERNAME, PASSWORD));
    }
}
