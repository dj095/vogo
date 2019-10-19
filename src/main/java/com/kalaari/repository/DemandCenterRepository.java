package com.kalaari.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.DemandCenter;

@Repository
public interface DemandCenterRepository extends CrudRepository<DemandCenter, Long> {

    @Query(value = "select * from demand_center where ST_DWithin(ST_Point(:request_lng, :request_lat), ST_Point(lng, lat), radius) order by ST_Distance(ST_Point(:request_lng, :request_lat), ST_Point(lng, lat)) desc limit 1",
            nativeQuery = true)
    DemandCenter getNearestDemandCenter(@Param("request_lat") Double lat, @Param("request_lng") Double lng);

    DemandCenter findById(Long id);

    DemandCenter findByName(String name);
}