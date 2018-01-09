package com.example.dmp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @version "\$Id$" kenan
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private String id;
    private String breId;
    private String dmpId;
    public String interest;


    public String getInterest() {
        return interest;
    }

    public void setInterest(final String interest) {
        this.interest = interest;
    }

    public String getId() {
        return id;
    }



    public void setId(final String id) {
        this.id = id;
    }

    public String getBreId() {
        return breId;
    }

    public void setBreId(final String breId) {
        this.breId = breId;
    }

    public String getDmpId() {
        return dmpId;
    }

    public void setDmpId(final String dmpId) {
        this.dmpId = dmpId;
    }

}
