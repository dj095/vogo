package com.kalaari.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.DemandCenter;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.entity.db.DemandLanePrediction;
import com.kalaari.repository.DemandCenterPredictionRepository;
import com.kalaari.repository.DemandCenterRepository;
import com.kalaari.repository.DemandLanePredictionRepository;

@Service
public class DemandCenterService {

    @Autowired
    private DemandCenterRepository demandCenterRepository;

    @Autowired
    private DemandCenterPredictionRepository demandCenterPredictionRepository;

    @Autowired
    private DemandLanePredictionRepository demandLanePredictionRepository;

    public List<DemandCenterPrediction> getTop10DemandCenterPredictions(Time currentTime) {
        return demandCenterPredictionRepository
                .findTop10ByFromTimeLessThanAndToTimeGreaterThanOrderByIdleWaitMins(currentTime, currentTime);
    }

    public List<DemandLanePrediction> getLanePredictions(Long fromDcId, Long toDcId, Time currentTime) {
        return demandLanePredictionRepository
                .findAllByFromDemandCenterIdAndToDemandCenterIdAndFromTimeLessThanAndToTimeGreaterThanOrderByEstimatedDemandDesc(
                        fromDcId, toDcId, currentTime, currentTime);
    }

    public DemandCenter getDemandCenterById(Long dcId) {
        return demandCenterRepository.findOne(dcId);
    }

    public DemandCenter getNearestDemandCenter(Double lat, Double lng) {
        return demandCenterRepository.getNearestDemandCenter(lat, lng);
    }

    public List<DemandCenter> getAllDemandCenters() {
        List<DemandCenter> demandCenters = new ArrayList<>();
        for (DemandCenter demandCenter : demandCenterRepository.findAll()) {
            demandCenters.add(demandCenter);
        }
        return demandCenters;
    }

    public List<DemandCenterPrediction> getDemandCenterPredictionAroundTime(Time time) {
        return demandCenterPredictionRepository.findAllByFromTimeLessThanAndToTimeGreaterThanOrderByIdleWaitMins(time,
                time);
    }

    public List<DemandLanePrediction> getDemandLanePredictionAroundTime(Time time) {
        return demandLanePredictionRepository
                .findAllByFromTimeLessThanAndToTimeGreaterThanOrderByEstimatedDemandDesc(time, time);
    }
}