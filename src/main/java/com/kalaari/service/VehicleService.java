package com.kalaari.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.Vehicle;
import com.kalaari.repository.VehicleRepository;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllNearbyVehicles(Double lat, Double lng) {
        return vehicleRepository.getAllNearbyVehicles(lat, lng);
    }
}