package com.kalaari.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.Vehicle;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

    @Query(value = "select * from vehicle_location where ST_DWithin(ST_POINT(lng, lat), ST_Point(:request_lng, :request_lat), radius) and p_timestamp > :start_time and p_timestamp < :end_time",
            nativeQuery = true)
    List<Vehicle> getAllNearbyVehiclesAroundTime(@Param("request_lat") Double lat, @Param("request_lng") Double lng,
            @Param("start_time") Date startTime, @Param("end_time") Date endTime);
}