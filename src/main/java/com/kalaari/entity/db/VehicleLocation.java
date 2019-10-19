package com.kalaari.entity.db;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "vehicle_location")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
public class VehicleLocation extends BaseEntity<Long> {

    @Column(name = "vehicle_number")
    private Long vehicleNumber;

    @Column(name = "idleSince")
    private Date idleSince;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "p_timestamp")
    private Time pTimestamp;
}