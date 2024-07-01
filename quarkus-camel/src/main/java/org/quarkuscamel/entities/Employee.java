package org.quarkuscamel.entities;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import jakarta.enterprise.context.ApplicationScoped;


@CsvRecord(separator = ",", crlf = "UNIX", generateHeaderColumns = true, skipFirstLine = true)
public class Employee {


    @DataField(pos = 1, columnName = "ID")
    private int id;

    @DataField(pos = 2, columnName = "Name")
    private String name;

    @DataField(pos = 3, columnName = "Position")
    private String position;

     @DataField(pos = 4, columnName = "Salary")
     private long salary;



    

    public Employee(int id, String name, String position, long salary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    

    
}
