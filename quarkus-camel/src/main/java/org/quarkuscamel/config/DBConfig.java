package org.quarkuscamel.config;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DBConfig {

    @Inject
    DataSource dataSource;

    public CamelContext createCamelContext() throws Exception {
        SimpleRegistry registry = new SimpleRegistry();
        registry.bind("dataSource", dataSource);
        CamelContext camelContext = new DefaultCamelContext(registry);
        return camelContext;
    }

}
