package com.kalaari.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.Vehicle;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    @Query(value = "select * from vehicle where ST_DWithin()", nativeQuery = true)
    List<Vehicle> getAllNearbyVehicles(@Param("lat") Double lat, @Param("lng") Double lng);
}