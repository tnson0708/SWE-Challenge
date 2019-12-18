package com.example.demo.domain;

import javax.persistence.*;

@Entity
@Table(name = "person")
public class Person {
    public Person() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
