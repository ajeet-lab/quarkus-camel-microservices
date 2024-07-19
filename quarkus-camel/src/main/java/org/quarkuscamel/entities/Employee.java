package org.quarkuscamel.entities;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;



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


    public Employee() {}

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
