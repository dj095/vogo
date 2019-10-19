package com.kalaari.repository;

import com.kalaari.entity.db.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

//    @Query(value = "select * from vehicle where ", nativeQuery = true)
//    List<Vehicle> getAllNearbyVehicles(Double lat, Double lng);
}