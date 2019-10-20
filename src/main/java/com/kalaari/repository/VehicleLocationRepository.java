package com.kalaari.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.VehicleLocation;

@Repository
public interface VehicleLocationRepository extends CrudRepository<VehicleLocation, Long> {

    @Query(value = "select * from vehicle_location where ST_DWithin(cast(ST_Point(lng, lat) as geography), cast(ST_Point(:request_lng, :request_lat) as geography), 1000)",
            nativeQuery = true)
    List<VehicleLocation> getAllNearbyVehiclesAroundTime(@Param("request_lat") Double lat,
            @Param("request_lng") Double lng);

    @Query(value = "select * from vehicle_location", nativeQuery = true)
    List<VehicleLocation> getAllVehiclesAroundTime();

    VehicleLocation findByVehicleNumber(String vehicleNumber);
}