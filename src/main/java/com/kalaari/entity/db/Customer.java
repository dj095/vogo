package com.kalaari.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "customer")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
public class Customer extends BaseEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "no_of_rides")
    private Integer noOfRides;

    public boolean isNewCustomer() {
        return noOfRides == 0;
    }
}