package com.kalaari.service;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.Vehicle;
import com.kalaari.repository.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllNearbyVehiclesAroundTime(Double lat, Double lng, Date timeOfRequest) {
        return vehicleRepository.getAllNearbyVehiclesAroundTime(lat, lng, timeOfRequest,
                new DateTime(timeOfRequest).plusHours(1).toDate());
    }
}