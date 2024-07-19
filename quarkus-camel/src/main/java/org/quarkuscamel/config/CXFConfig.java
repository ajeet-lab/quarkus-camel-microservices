package org.quarkuscamel.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.apache.camel.CamelContext;
import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.jaxws.CxfComponent;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;


@ApplicationScoped
public class CXFConfig {
    public static final String SERVICE_ADDRESS = "http://www.dneonline.com/calculator.asmx";
    private CamelContext context;

    @Produces
    @ApplicationScoped
    @Named("cxfEndpoint")
    public CxfEndpoint myEndpoint() {
        CxfComponent cxfComponent = new CxfComponent(context);
        CxfEndpoint cxfEndpoint = new CxfEndpoint(SERVICE_ADDRESS, cxfComponent);
        cxfEndpoint.setWsdlURL("/calculator.wsdl");
        cxfEndpoint.setLoggingFeatureEnabled(true);
        cxfEndpoint.setServiceName("{http://tempuri.org/}Calculator");
        cxfEndpoint.setPortName("{http://tempuri.org/}CalculatorSoap");
        cxfEndpoint.setDataFormat(DataFormat.PAYLOAD);
        //cxfEndpoint.setDataFormat(DataFormat.CXF_MESSAGE);
        return cxfEndpoint;
    }
}
