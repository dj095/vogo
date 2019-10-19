package com.kalaari.service;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.VehicleLocation;
import com.kalaari.repository.VehicleLocationRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleLocationRepository vehicleLocationRepository;

    public List<VehicleLocation> getAllNearbyVehiclesAroundTime(Double lat, Double lng, Date timeOfRequest) {
        return vehicleLocationRepository.getAllNearbyVehiclesAroundTime(lat, lng, timeOfRequest,
                new DateTime(timeOfRequest).plusHours(1).toDate());
    }
}