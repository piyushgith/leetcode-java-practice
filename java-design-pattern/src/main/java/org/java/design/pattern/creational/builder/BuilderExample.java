package org.java.design.pattern.creational.builder;

public class BuilderExample {
    static void main(String[] args) {
        User user = new User.UserBuilder()
                .setName("Piyush Prasad")
                .setEmail("piyush@example.com")
                .setAge(18)
                .build();

        System.out.println(user);
    }
}
