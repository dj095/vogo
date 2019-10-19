package com.kalaari.entity.db;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "trip")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
public class Trip extends BaseEntity<Long> {

    @Column(name = "from_dc_id")
    private Long fromDcId;

    @Column(name = "to_dc_id")
    private Long toDcId;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "start_time", columnDefinition = "time")
    private Time startTime;

    @Column(name = "end_time", columnDefinition = "time")
    private Time endTime;

    @Column(name = "no_of_trips")
    private Integer noOfTrips;
}