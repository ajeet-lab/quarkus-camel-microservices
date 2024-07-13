## Project overview
1. Database CRUD operations
2. JSON to CSV conversion
3. CSV to JSON conversion
4. AMQ (ActiveMQ integration)
5. XML to JSON conversion
6. JSON to XML conversion
7. SOAP service : Convert Xml request to Soap request
8. SOAP service :  SOAP integration with Calculator WSDL
9. Data mapping with Jolt
10. SalesForce integration


### How to config more properties file in application.properties
* Add single properties file
```
# ADD PROPERTIES FILE
camel.component.properties.location=sql.properties
```

* Add multiple properties file
```
camel.component.properties.location=sql.properties,salesforce.properties
```
**Note:** We can add multiple properties files separated by commas.


### Create table in database
```
CREATE TABLE quarkus.employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    position VARCHAR(50) NOT NULL,
    salary long NOT NULL
);
```

### Configuration of the database and required dependency.
* In application.properties
```
#Database Configuration
quarkus.datasource.db-kind=mysql
quarkus.datasource.username=root
quarkus.datasource.password=root
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/quarkus?createDatabaseIfNotExist=true
quarkus.datasource.jdbc.driver=com.mysql.cj.jdbc.Driver
```

* Database Dependencies
```
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-mysql</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-sql</artifactId>
</dependency>
```
**Note:** We can use the **JDBC** dependency instead of **SQL** and make the necessary changes to the SQL query according to the **SQL/JDBC** dependency. The query is mentioned in the **sql.properties** file.

### ActiveMQ Configuration and required dependency.
```
# ActiveMQ Configuration
quarkus.camel.component.activemq.broker-url=tcp://localhost:61616
quarkus.camel.component.activemq.user=admin
quarkus.camel.component.activemq.password=admin
#quarkus.pooled-jms.pooling.enabled=false
```

* ActiveMQ Dependencies
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-activemq</artifactId>
</dependency>

<dependency>
    <groupId>org.messaginghub</groupId>
    <artifactId>pooled-jms</artifactId>
    <version>3.1.6</version>
</dependency>
```
**Note:** Here, the **pooled-jms** dependency is required; otherwise, it will throw an error.


### Salesforce configuration
* In salesforce.properties file
```
camel.component.salesforce.clientId=your-clientId
camel.component.salesforce.clientSecret=your-clientSecret
camel.component.salesforce.userName=your-salesforce-userName
camel.component.salesforce.password=your-salesforce-password
camel.component.salesforce.loginUrl=https://test.salesforce.com
camel.component.salesforce.packages=org.apache.camel.component.salesforce.api.dto
camel.component.salesforce.authenticationType=JWT
camel.component.salesforce.keystore.resource=your-jks-path
camel.component.salesforce.keystore.password=your-jks-keystore-password
camel.component.salesforce.keystore.type=JKS
camel.component.salesforce.keystore.provider=SUN
```
* Salesforce dependency
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-salesforce</artifactId>
</dependency>
```

### Rest configuration with netty-http
* Configuration
```
#Rest configuration
restConfiguration()
                .component("netty-http")
                .host("localhost")
                .port(8081);
```

```
rest("/api/v1").get("/users")
                .bindingMode(RestBindingMode.auto).to("direct:getAllData");
```
Here, We used **bindingMode(RestBindingMode.auto)**, which means we **consume** and **produce** JSON data, but the **Jackson** dependency is required for it.

```
 <dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-jackson</artifactId>
</dependency>
```

### Data conversion configuration
#### JSON to CSV and CSV to JSON conversion
* JSON to CSV with CSV dependency
```
import org.apache.camel.dataformat.csv.CsvDataFormat;

@Override
    public void configure() throws Exception {
        
        CsvDataFormat csvFormat = new CsvDataFormat();
        csvFormat.setDelimiter(',');
        csvFormat.setHeader(new String[]{"id", "name", "position", "salary"}); // Enable maps format for CSV

        from("direct:jsontocsvwithcsv")
                .setBody(simple("{{sql.getAllData}}"))
                .toD("sql:${body}")
                .log("Get data by id >>> TotalSize : ${body.size()}  >>>>>> and body: ${body}")
                .marshal(csvFormat)
                .to("file:work/output?fileName=user_csv.csv&fileExist=Override")
                .setHeader("message", simple("CSV_CREATED"))
                .log("Csv file created successfully !!");
    }
```
* CSV to JSON with CSV dependency
```
import org.apache.camel.dataformat.csv.CsvDataFormat;

 @Override
    public void configure() throws Exception {

        CsvDataFormat csvDataFormat = new CsvDataFormat();
        csvDataFormat.setUseMaps(true);

        from("direct:csvtojsonwithcsv")
                .pollEnrich().simple("file:work/output?fileName=user_csv.csv&noop=true")
                .log("Data received from CSV file >>> ${body}")
                .unmarshal(csvDataFormat)
                .log("Data successfully converted into json !!");
    }
```

* Csv dependency
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-jackson</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-csv</artifactId>
</dependency>
```



* JSON to CSV with Bindy dependency
```
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

@Override
public void configure() throws Exception {
    BindyCsvDataFormat bindy = new BindyCsvDataFormat(Employee.class);
    bindy.setLocale("en");
    from("direct:jsontocsvwithbindy")
            .routeId("jsonToCsvWithWithBindyRoute")
            .setBody(simple("{{sql.getAllData}}"))
            .toD("sql:${body}")
            .log("Get data by id >>> ${body.size()}") // Log retrieved data         
            .process(exchange -> {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> resultList = exchange.getIn().getBody(List.class);
                List<Employee> employees = new ArrayList<>();
                for (Map<String, Object> empMap : resultList) {
                    int id = (int) empMap.get("id");
                    String name = (String) empMap.get("name");
                    String position = (String) empMap.get("position");
                    long salary = 1234444L; // Adjust if the salary is stored differently    
                    Employee employee = new Employee(id, name, position, salary);
                    employees.add(employee);
                }
                exchange.getIn().setBody(employees);
            })
            
            .marshal(bindy)
            .toD("file:work/output?fileName=user_bindy.csv")
            .setHeader("message", simple("CSV_CREATED"))
            .log("CSV file created successfully !!");
}
```

* CSV to JSON with Bindy dependency
```
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

@Override
public void configure() throws Exception {
    BindyCsvDataFormat bindy = new BindyCsvDataFormat(Employee.class);

    from("direct:csvtojsonwithbindy")
            .routeId("csvToJsonWithBindyRoute")
            .pollEnrich().simple("file:work/output?fileName=user_bindy.csv&noop=true")
            .log("Data received from CSV file >>> ${body}")
            .unmarshal(bindy)
            .log("Data successfully converted into json !!");
}
```

In the above, for data conversion, we used the **Employee class** as an object. The Employee class is defined below.

* Employee.class
```
@CsvRecord(separator = ",", generateHeaderColumns = true, skipFirstLine = true)
public class Employee {

    @DataField(pos = 1)
    private int id;

    @DataField(pos = 2)
    private String name;

    @DataField(pos = 3)
    private String position;

    @DataField(pos = 4)
    private long salary;
    
    //Add here, NoArgsConstructor and AllArgsConstructor
    
    //Add here, Getter and Setter 
}
```

* Bindy dependency
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-jackson</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-bindy</artifactId>
</dependency>
```

#### JSON to XML and XML to JSON conversion
* JSON to XML 
```
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;

@Override
public void configure() throws Exception {
    JacksonDataFormat jsonDataFormat = new JacksonDataFormat();
    jsonDataFormat.setUnmarshalType(Person.class);

    JacksonXMLDataFormat jacksonXmlDataFormat = new JacksonXMLDataFormat();
    jacksonXmlDataFormat.setUnmarshalType(Person.class);

    from("direct:jsontoxml")
            .routeId("jsonToXmlRoute")
            .marshal(jsonDataFormat)
            .unmarshal(jsonDataFormat) // Unmarshal JSON to Data object
            .log("After marshling >> ${body}")
            .marshal(jacksonXmlDataFormat)
            .convertBodyTo(String.class);
}
```

* XML to JSON
```
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;

@Override
public void configure() throws Exception {
    JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Person.class);
    JacksonXMLDataFormat jacksonXmlDataFormat = new JacksonXMLDataFormat(Person.class);

    from("direct:xmltojson")
            .routeId("xmlToJsonRoute")
            .unmarshal(jacksonXmlDataFormat)
            .marshal(jsonDataFormat)
            .log("After JSON marshalling :: ${body}");
}
```

In the above, for data conversion, we used the **Person class** as an object. The Person class is defined below.

* Person class
```
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Person {

    @JacksonXmlProperty
    private int id;

    @JacksonXmlProperty
    private String name;

    @JacksonXmlProperty
    private int age;

    //Add here, Getter and Setter
}
```

* Dependency
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-jackson</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-jacksonxml</artifactId>
</dependency>
```

### XML request to SOAP request conversion
```
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jacksonxml.JacksonXMLDataFormat;

@Override
public void configure() throws Exception {
    JacksonXMLDataFormat jacksonXmlDataFormat = new JacksonXMLDataFormat(SoapEnvelope.class);
    JacksonDataFormat jacksonDataFormat = new JacksonDataFormat(Person.class);
    from("direct:xmltosoap")
            .routeId("xmlToSoapRoute")
            .unmarshal().jacksonXml()
            .marshal(jacksonDataFormat)
            .unmarshal(jacksonDataFormat)

            .process(exchange -> {
                Person person = exchange.getIn().getBody(Person.class);
                // Create a new instance of SoapEnvelope
                SoapEnvelope soapEnvelope = new SoapEnvelope();

                // Create a new instance of SoapBody and set the unmarshalled element
                SoapBody soapBody = new SoapBody();
                soapBody.setPerson(person);
                soapEnvelope.setBody(soapBody);

                // Create a new instance of SoapHeader and add any necessary header elements
                SoapHeader soapHeader = new SoapHeader();
                soapHeader.setUsername(exchange.getIn().getHeader("username", String.class));
                soapHeader.setPassword(exchange.getIn().getHeader("password", String.class));
                soapEnvelope.setHeader(soapHeader);

                exchange.getIn().setBody(soapEnvelope);
            })
            .marshal(jacksonXmlDataFormat)
            .log("After SOAP marshalling :: ${body}");
}
```
Here, we used the SoapEnvelope class for XML to SOAP request conversion, and the Person class is used for defining the type of data it contains. 

* SoapEnvelope.class
```
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapEnvelope {
    @JacksonXmlProperty(localName = "Header")
    private SoapHeader header;

    @JacksonXmlProperty(localName = "Body")
    private SoapBody body;
    
    // Add Here, Getter and Setter
}
```

The **SoapEnvelope** class contains two types of data: the first one is **SoapHeader** and the second one is **SoapBody**.

* SoapHeader.class
```
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapHeader {
    // Define any necessary header fields here
    @JacksonXmlProperty
    private String  username;

    @JacksonXmlProperty
    private String  password;
    
    // Add here, Getter and Setter
}
```

* SoapBody.class
```
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")
public class SoapBody {
    @JacksonXmlProperty(localName = "Person")
    private Person person;

    // Add Here, Getter and Setter
}
```

* Person.class
```
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Person {

    @JacksonXmlProperty
    private int id;

    @JacksonXmlProperty
    private String name;

    @JacksonXmlProperty
    private int age;
    
    // Add here, Getter and Setter
}
```

* Dependency
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-jackson</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-jacksonxml</artifactId>
</dependency>
```

### CXF configuration
* CXFConfig.class
```
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
```

* CalculatorRoute.class
```
from("direct:calculator")
            .routeId("soapCalculatorRoute")
            .doTry()
                .removeHeaders("*", "Content-Type|CamelHttpUri")
                .unmarshal().json(JsonLibrary.Jackson)
                .log("SoapCalculator001 :: After marshalling : ${body}")
                .bean("utils", "checkEmptyOrNullValue")
                .bean("utils", "setOperationNameAndSpace")
                .to("velocity:{{calculator}}").convertBodyTo(String.class)
                .log("SoapCalculator002 :: Soap request is : ${body}")
                .to(ENDPOINT)
                .log("SoapCalculator003 :: Soap request is : ${body}")
                .unmarshal().jacksonXml().marshal().json(JsonLibrary.Jackson)
                .log("SoapCalculator004 :: Result is : ${body}")
            .doCatch(IllegalArgumentException.class)
                .log("SoapCalculatorException001 :: Inside the exception block : ${exception.message}")
                .setProperty("expMsg", simple("${exception.message}"))
                .setHeader("Content-Type", simple("application/json"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, simple("400"))
                .bean("utils", "exceptionMessage")
                .marshal().json(JsonLibrary.Jackson)
                .log("SoapCalculatorException002 :: message: ${body}")
            .endDoTry();
}
```

* Payload
```
<tem:${headers.operationName} xmlns:tem="http://tempuri.org/">
    <tem:intA>$body.intA</tem:intA>
    <tem:intB>$body.intB</tem:intB>
</tem:${headers.operationName}>
```

* Dependency
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-cxf-soap</artifactId>
</dependency>
```

* Add a plugin inside the plugins section in the pom.xml.
```
<!--Generate soap to java file-->
<plugin>
    <groupId>org.apache.cxf</groupId>
    <artifactId>cxf-codegen-plugin</artifactId>
    <version>3.5.0</version>
    <executions>
        <execution>
            <id>generate-sources</id>
            <phase>generate-sources</phase>
            <configuration>
                <sourceRoot>
                    ${basedir}/src/main/java
                </sourceRoot>
                <wsdlOptions>
                    <wsdlOption>
                        <wsdl>
                            ${basedir}/src/main/resources/calculator.wsdl
                        </wsdl>
                    </wsdlOption>
                </wsdlOptions>
            </configuration>
            <goals>
                <goal>wsdl2java</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
**Note:** Add the above plugin inside the plugins section and run the command below once. After generating the Java classes, comment out the above plugin.
```
mvn clean install || mvn clean package
```