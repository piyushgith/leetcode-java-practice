package com.java.example.common;

import java.io.Serializable;
import java.util.Objects;

public class Employee implements Serializable {

    private int id;
    private String name;
    private String department;
    private int age;

    public Employee(int id, String name,String department, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;
        return getId() == employee.getId()
                && getAge() == employee.getAge()
                && Objects.equals(getName(), employee.getName());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + Objects.hashCode(getName());
        result = 31 * result + getAge();
        return result;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
