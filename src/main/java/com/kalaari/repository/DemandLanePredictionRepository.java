package com.kalaari.repository;

import java.sql.Time;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.DemandLanePrediction;

@Repository
public interface DemandLanePredictionRepository extends CrudRepository<DemandLanePrediction, Long> {

    List<DemandLanePrediction> findAllByFromDemandCenterIdAndToDemandCenterIdAndFromTimeLessThanAndToTimeGreaterThanOrderByEstimatedDemandDesc(
            Long fromDcId, Long toDcId, Time fromTimeLessThan, Time toTimeGreaterThan);
}