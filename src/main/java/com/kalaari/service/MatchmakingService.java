package com.kalaari.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kalaari.entity.db.CustomerLanePreference;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.entity.db.Vehicle;

@Service
public class MatchmakingService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DemandCenterService demandCenterService;

    public List<Vehicle> getVehicles(Long customerId, Long demandCenterId) {

        List<Vehicle> allocatedVehicles = new ArrayList<>();
        List<CustomerLanePreference> lanePreferences = customerService.getCustomerLanePreferences(customerId,
                demandCenterId);
        if (!CollectionUtils.isEmpty(lanePreferences)) {

            List<DemandCenterPrediction> demandCenterPredictions = demandCenterService
                    .getDemandCenterPredictions(new Time(new Date().getTime()));

            for (CustomerLanePreference lanePreference : lanePreferences) {
                Long toDc = lanePreference.getToDemandCenter();
                Integer weight = lanePreference.getWeight();

                for (DemandCenterPrediction demandCenterPrediction : demandCenterPredictions) {
                    demandCenterPrediction.getIdleWaitMins();
                }
            }
        }
        return allocatedVehicles;
    }
}