package com.kalaari.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.VehicleLocation;
import com.kalaari.repository.VehicleLocationRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleLocationRepository vehicleLocationRepository;

    public List<VehicleLocation> getAllNearbyVehiclesAroundTime(Double lat, Double lng) {
        return vehicleLocationRepository.getAllNearbyVehiclesAroundTime(lat, lng);
    }

    public List<VehicleLocation> getVehiclesAroundTime() {
        return vehicleLocationRepository.getAllVehiclesAroundTime();
    }
}