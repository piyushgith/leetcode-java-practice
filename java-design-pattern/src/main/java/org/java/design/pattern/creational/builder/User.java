package org.java.design.pattern.creational.builder;

public class User {
    private String name;
    private String email;
    private int age;

    private User(UserBuilder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.age = builder.age;
    }

    public static class UserBuilder {
        private String name;
        private String email;
        private int age;

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public String toString() {
        return name + " | " + email + " | " + age;
    }
}