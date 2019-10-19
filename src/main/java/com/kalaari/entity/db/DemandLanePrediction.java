package com.kalaari.entity.db;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "demand_lane_prediction")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted=false")
public class DemandLanePrediction extends BaseEntity<Long> {

    @Column(name = "from_demand_center_id")
    private Long fromDemandCenterId;

    @Column(name = "to_demand_center_id")
    private Long toDemandCenterId;

    @Column(name = "from_time", columnDefinition = "time")
    private Time fromTime;

    @Column(name = "to_time", columnDefinition = "time")
    private Time toTime;

    @Column(name = "estimated_demand")
    private Long estimatedDemand;

    @Column(name = "profit_margin")
    private Double profitMargin;
}