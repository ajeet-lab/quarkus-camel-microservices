package org.quarkuscamel.routes.csvroutes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.quarkuscamel.entities.Employee;

public class JsonToCsvWithBindy extends RouteBuilder {

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
                // Write to CSV file with append mode
                .toD("file:work/output?fileName=user_bindy.csv")
                .setHeader("message", simple("CSV_CREATED"))
                .bean("utils", "csvJsonAmqMessage");
    }

}
