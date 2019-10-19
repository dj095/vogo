package com.kalaari.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kalaari.entity.db.Customer;
import com.kalaari.entity.db.CustomerLanePreference;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.entity.db.Vehicle;
import com.kalaari.exception.KalaariErrorCode;
import com.kalaari.exception.KalaariException;

@Service
public class MatchmakingService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DemandCenterService demandCenterService;

    public List<Vehicle> getVehicles(Long customerId, Long demandCenterId, Date timeOfRequest) throws KalaariException {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new KalaariException(KalaariErrorCode.BAD_REQUEST, "Customer: " + customerId + " does not exist !");
        }
        List<CustomerLanePreference> lanePreferences = customerService.getCustomerLanePreferences(customerId,
                demandCenterId);
        if (CollectionUtils.isEmpty(lanePreferences)) {

        }
        List<DemandCenterPrediction> demandCenterPredictions = demandCenterService
                .getDemandCenterPredictions(new Time(timeOfRequest.getTime()));
        return allocateVehicles(customer, lanePreferences, demandCenterPredictions);
    }

    public List<Vehicle> allocateVehicles(Customer customer, List<CustomerLanePreference> lanePreferences,
            List<DemandCenterPrediction> demandCenterPredictions) {
        List<Vehicle> allocatedVehicles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lanePreferences)) {
            for (CustomerLanePreference lanePreference : lanePreferences) {
                Long toDc = lanePreference.getToDemandCenter();
                Integer weight = lanePreference.getWeight();

            }
        }
        return null;
    }
}