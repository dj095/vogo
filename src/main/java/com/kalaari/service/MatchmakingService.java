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

    @Autowired
    private VehicleService vehicleService;

    public List<Vehicle> getVehicles(Long customerId, Double customerLat, Double customerLng, Long demandCenterId,
            Date timeOfRequest) throws KalaariException {

        // FIND CUSTOMER
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new KalaariException(KalaariErrorCode.BAD_REQUEST, "Customer: " + customerId + " does not exist !");
        }

        // GET VEHICLES TO ALLOCATE
        List<Vehicle> vehiclesToAllocate = vehicleService.getAllNearbyVehiclesAroundTime(customerLat, customerLng,
                timeOfRequest);
        if (CollectionUtils.isEmpty(vehiclesToAllocate)) {
            throw new KalaariException(KalaariErrorCode.BAD_REQUEST, "No Vehices were found to allocate around time: "
                    + timeOfRequest + ", for lat: " + customerLat + " lng: " + customerLng);
        }

        // GET CUSTOMER LANE PREF
        List<CustomerLanePreference> lanePreferences = customerService.getCustomerLanePreferences(customerId,
                demandCenterId);

        // GET DC PREDICTIONS
        List<DemandCenterPrediction> demandCenterPredictions = demandCenterService
                .getDemandCenterPredictions(new Time(timeOfRequest.getTime()));

        // ALLOCATE
        return allocateVehicles(customer, lanePreferences, demandCenterPredictions, vehiclesToAllocate);
    }

    public List<Vehicle> allocateVehicles(Customer customer, List<CustomerLanePreference> lanePreferences,
            List<DemandCenterPrediction> demandCenterPredictions, List<Vehicle> vehiclesToAllocate) {
        List<Vehicle> allocatedVehicles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lanePreferences)) {
            for (CustomerLanePreference lanePreference : lanePreferences) {
                Long toDc = lanePreference.getToDemandCenter();
                Integer weight = lanePreference.getWeight();

            }
        }
        return allocatedVehicles;
    }
}