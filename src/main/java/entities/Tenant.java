package entities;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tenant {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private int phoneNum;
    private String job;



}
