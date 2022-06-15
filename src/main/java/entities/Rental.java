package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String startDate;
    private String endDate;
    private int priceAnnual;
    private int deposit;
    private String contactPerson;


}
