package com.kalaari.service;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.repository.DemandCenterPredictionRepository;
import com.kalaari.repository.DemandCenterRepository;

@Service
public class DemandCenterService {

    @Autowired
    private DemandCenterRepository demandCenterRepository;

    @Autowired
    private DemandCenterPredictionRepository demandCenterPredictionRepository;

    public List<DemandCenterPrediction> getDemandCenterPredictions(Time currentTime) {
        return demandCenterPredictionRepository
                .findAllByFromTimeLessThanAndToTimeGreaterThanOrderByIdleWaitMinsDesc(currentTime, currentTime);
    }
}