package com.viloveul.app;

import com.viloveul.context.ApplicationContainer;
import com.viloveul.context.ViloveulConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Objects;

@EnableAsync
@EnableCaching
@Import(ViloveulConfiguration.class)
@EntityScan(basePackages = {"com.viloveul.module", "com.viloveul.app"})
@EnableJpaRepositories(basePackages = {"com.viloveul.module", "com.viloveul.app"})
@SpringBootApplication(scanBasePackages = {"com.viloveul.module", "com.viloveul.app"})
public class ExampleApplication extends SpringBootServletInitializer implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private CacheManager cacheManager;

    public static void main(String... args) {
        ApplicationContainer.setup(new ExtendedSpringApplication(), args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources( ExampleApplication.class);
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ServletWebServerInitializedEvent) {
            Objects.requireNonNull(this.cacheManager.getCache("AccessControlEvaluator")).clear();
        }
    }

    private static class ExtendedSpringApplication extends SpringApplication implements ApplicationContainer.ExtendedSpringApplication {
        private ExtendedSpringApplication() {
            super(ExampleApplication.class);
        }
    }
}
