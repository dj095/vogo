package com.kalaari.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Long, DemandCenterPrediction> dcIdToDcp = new HashMap<>();

    public List<DemandCenterPrediction> getTop10DemandCenterPredictions() {
        return demandCenterPredictionRepository.findTop10ByOrderByIdleWaitMins();
    }

    public List<DemandLanePrediction> getLanePredictions(Long fromDcId, Long toDcId) {
        return demandLanePredictionRepository
                .findAllByFromDemandCenterIdAndToDemandCenterIdOrderByEstimatedDemandDesc(fromDcId, toDcId);
    }

    public DemandCenter getDemandCenterById(Long dcId) {
        return demandCenterRepository.findOne(dcId);
    }

    public DemandCenterPrediction getDemandCenterPredictionById(Long dcId) {
        if (!dcIdToDcp.containsKey(dcId)) {
            dcIdToDcp.put(dcId, demandCenterPredictionRepository.findByDemandCenterIdOrderByIdleWaitMins(dcId));
        }
        return dcIdToDcp.get(dcId);
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

    public List<DemandCenterPrediction> getDemandCenterPredictionAroundTime() {
        return demandCenterPredictionRepository.findAllByOrderByIdleWaitMins();
    }

    public List<DemandLanePrediction> getDemandLanePredictionAroundTime() {
        return demandLanePredictionRepository.findAllByOrderByEstimatedDemandDesc();
    }
}