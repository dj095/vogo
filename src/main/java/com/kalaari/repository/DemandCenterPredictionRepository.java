package com.kalaari.repository;

import java.sql.Time;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.DemandCenterPrediction;

@Repository
public interface DemandCenterPredictionRepository extends CrudRepository<DemandCenterPrediction, Long> {

    List<DemandCenterPrediction> findTop10ByFromTimeLessThanAndToTimeGreaterThanOrderByIdleWaitMinsDesc(
            Time fromTimeLessThan, Time toTimeGreaterThan);
}