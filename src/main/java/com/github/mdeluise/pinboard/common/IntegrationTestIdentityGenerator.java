package com.github.mdeluise.pinboard.common;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@Component
public class IntegrationTestIdentityGenerator extends IdentityGenerator {
    private String tableName;


    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Serializable id =
            (Serializable) session.getEntityPersister(null, object).getClassMetadata().getIdentifier(object, session);
        if (id != null) {
            return id;
        }

        var ref = new Object() {
            Long generatedId;
        };
        session.doWork(connection -> {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select count(id) from " + tableName);
                if (resultSet.next()) {
                    ref.generatedId = resultSet.getLong(1) + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalIdentifierException("Error while generating identifier for entry of table " + tableName);
            }
        });
        return ref.generatedId;
    }


    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(type, params, serviceRegistry);
        tableName = params.getProperty("tableName");
    }
}