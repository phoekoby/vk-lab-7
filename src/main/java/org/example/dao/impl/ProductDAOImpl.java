package org.example.dao.impl;


import com.google.inject.Inject;
import org.example.config.DBCredentials;
import org.example.dao.OrganizationDAO;
import org.example.dao.ProductDAO;

import org.example.mapper.ProductMapper;
import org.example.pojo.OrganizationDTO;
import org.example.pojo.ProductDTO;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.stream.Collectors;

import static generated.Tables.*;


public class ProductDAOImpl implements ProductDAO {

    private final DBCredentials dbCredentials;

    private final ProductMapper productMapper;

    private final OrganizationDAO organizationDAO;

    @Inject
    public ProductDAOImpl(DBCredentials dbCredentials, ProductMapper productMapper, OrganizationDAO organizationDAO) {
        this.dbCredentials = dbCredentials;
        this.productMapper = productMapper;
        this.organizationDAO = organizationDAO;
    }

    @Override
    public Collection<ProductDTO> getAll() {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            return context
                    .select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.AMOUNT, ORGANIZATION.NAME)
                    .from(PRODUCT)
                    .rightJoin(ORGANIZATION)
                    .on(PRODUCT.ORGANIZATION_ID.eq(ORGANIZATION.ID))
                    .fetchStream()
                    .map(productMapper::toEntityWithOrganizationName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ProductDTO> getById(@NotNull Long id) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            final @NotNull Optional<Record4<Long, String, Integer, String>> record = Optional.ofNullable(context
                    .select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.AMOUNT, ORGANIZATION.NAME)
                    .from(PRODUCT)
                    .rightJoin(ORGANIZATION)
                    .on(PRODUCT.ORGANIZATION_ID.eq(ORGANIZATION.ID))
                    .where(PRODUCT.ID.eq(id))
                    .fetchOne());
            return record.map(productMapper::toEntityWithOrganizationName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Long id) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .deleteFrom(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDTO update(@NotNull ProductDTO value) {
        if (value.getId() == null || getById(value.getId()).isEmpty()) {
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .update(PRODUCT)
                    .set(PRODUCT.NAME, value.getName())
                    .set(PRODUCT.AMOUNT, value.getAmount())
                    .where(PRODUCT.ID.eq(value.getId()))
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDTO save(@NotNull ProductDTO value) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context.
                    insertInto(PRODUCT, PRODUCT.NAME, PRODUCT.ORGANIZATION_ID, PRODUCT.AMOUNT)
                    .values(value.getName(), organizationDAO
                            .save(new OrganizationDTO(value.getOrganizationName()))
                            .getId(), value.getAmount())
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
