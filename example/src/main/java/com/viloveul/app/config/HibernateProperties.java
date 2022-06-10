package com.viloveul.app.config;

import com.viloveul.context.behaviour.EntityEventInterceptor;
import org.hibernate.EmptyInterceptor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration(
    proxyBeanMethods = false
)
public class HibernateProperties implements HibernatePropertiesCustomizer {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(
            AvailableSettings.INTERCEPTOR,
            new EntityEventInterceptor(this.applicationEventPublisher, EmptyInterceptor.INSTANCE)
        );
    }
}
