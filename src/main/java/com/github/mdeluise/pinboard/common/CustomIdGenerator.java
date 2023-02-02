package com.github.mdeluise.pinboard.common;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class CustomIdGenerator extends IdentityGenerator {
    private final String activeProfile;
    private final IntegrationTestIdentityGenerator integrationTestIdentityGenerator;


    @Autowired
    public CustomIdGenerator(@Value("${spring.profiles.active:}") String activeProfile,
                             IntegrationTestIdentityGenerator integrationTestIdentityGenerator) {
        this.activeProfile = activeProfile;
        this.integrationTestIdentityGenerator = integrationTestIdentityGenerator;
    }


    @Override
    public Object generate(SharedSessionContractImplementor s, Object obj) {
        return "integration".equals(activeProfile) ?
                   integrationTestIdentityGenerator.generate(s, obj) :
                   super.generate(s, obj);
    }


    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        integrationTestIdentityGenerator.configure(type, params, serviceRegistry);
    }
}
