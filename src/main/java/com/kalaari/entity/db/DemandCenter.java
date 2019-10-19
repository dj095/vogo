package com.kalaari.entity.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "demand_center")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
public class DemandCenter extends BaseEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "no_of_vehicles")
    private Long noOfVehicles;

    @Column(name = "radius")
    private Double radius;
}