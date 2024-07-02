package org.quarkuscamel.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("utils")
@RegisterForReflection
public class Utils {

    private final Logger log = LoggerFactory.getLogger(Utils.class);

    public void createData(Exchange ex) throws JsonProcessingException {
        List<Map<String, Object>> mapper = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "Mike Brown " + random.nextInt(100));
            map.put("position", "Product Manager " + random.nextInt(100));
            map.put("salary", random.nextInt(200000) + 50000);
            mapper.add(map);
        }
        ex.getIn().setBody(mapper);
    }

    public void pushIntoQueueSuccessMSG(Exchange ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", 200);
        map.put("status", "success");
        map.put("message", "Data pushed into queue successfully !!");
        ex.getIn().setBody(map);
    }

    public void csvCreatedSuccessMSG(Exchange ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", 200);
        map.put("status", "success");
        map.put("message", "Csv file created successfully !!");
        ex.getIn().setBody(map);
    }

    public void objectToArray(Exchange ex) {
        @SuppressWarnings("unchecked")
        Map<String, Object> object = ex.getIn().getBody(Map.class);
        List<Map<String, Object>> objectToArray = new ArrayList<>();
        objectToArray.add(object);
        ex.getIn().setBody(objectToArray);
    }

    public void message(Exchange ex) {
        Map<String, Object> map = new HashMap<>();
        String message = ex.getIn().getHeader("message", String.class);
        int camelSqlUpdateCount = ex.getIn().getHeader("CamelSqlUpdateCount", Integer.class);
        int statusCode = 200;
        String status = "SUCCESS";

        if (message.equalsIgnoreCase("CREATED")) {
            message = "Data pushed into queue successfully !!";
        }else if (camelSqlUpdateCount == 1) {
            if (message.equalsIgnoreCase("UPDATED")) {
                message = "Data updated successfully !!";
            }

            if (message.equalsIgnoreCase("DELETED")) {
                message = "Data deleted successfully !!";
            }

        } else {
            Integer id = ex.getIn().getHeader("id", Integer.class);
            if (message.equalsIgnoreCase("UPDATED")) {
                statusCode = 400;
                status = "FAILURE";
                message = "Data did not update successfully with given id : " + id;
            }
            if (message.equalsIgnoreCase("DELETED")) {
                statusCode = 400;
                status = "FAILURE";
                message = "Data did not delete successfully with given id : " + id;
            }
        }

        map.put("statusCode", statusCode);
        map.put("status", status);
        map.put("message", message);
        ex.getIn().setBody(map);
    }
}
