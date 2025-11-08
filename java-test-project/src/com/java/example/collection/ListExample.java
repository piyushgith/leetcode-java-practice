package com.java.example.collection;

import com.java.example.common.Employee;
import com.java.example.common.MyUtil;

import java.util.List;

public class ListExample {
    public static void main(String[] args) {

        List<Employee> employeesList = MyUtil.getEmployees();

        for (Employee emp : employeesList) {
            System.out.println(emp);
        }





    }




}
