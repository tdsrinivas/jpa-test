package com.srini.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String guid;
    private String firstName;
    private String lastName;
}
