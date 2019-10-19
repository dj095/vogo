package com.kalaari.service;

import java.sql.Time;
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

    public List<VehicleLocation> getAllNearbyVehiclesAroundTime(Double lat, Double lng, Time timeOfRequest) {
        return vehicleLocationRepository.getAllNearbyVehiclesAroundTime(lat, lng, timeOfRequest,
                new Time(new DateTime(timeOfRequest).plusHours(1).getMillis()));
    }

    public List<VehicleLocation> getVehiclesAroundTime(Time timeOfRequest) {
        return vehicleLocationRepository.getAllVehiclesAroundTime(timeOfRequest,
                new Time(new DateTime(timeOfRequest).plusHours(1).getMillis()));
    }
}