package org.example.config;

import com.google.inject.AbstractModule;
import org.example.dao.OrganizationDAO;
import org.example.dao.ProductDAO;
import org.example.dao.impl.OrganizationDAOImpl;
import org.example.dao.impl.ProductDAOImpl;
import org.example.filter.PostFilter;
import org.example.server.ServerCreator;
import org.example.service.MigrationService;
import org.example.servlets.ProductsServlet;

import static org.example.config.DbConstants.*;

public class ProductModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MigrationService.class);
        bind(DBCredentials.class).toInstance(new DBCredentials(CONNECTION, DB_NAME, USERNAME, PASSWORD));
        bind(OrganizationDAO.class).to(OrganizationDAOImpl.class);
        bind(ProductDAO.class).to(ProductDAOImpl.class);
        bind(ProductsServlet.class).asEagerSingleton();
        bind(PostFilter.class).asEagerSingleton();
        bind(ServerCreator.class).asEagerSingleton();
    }
}
