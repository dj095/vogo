package com.kalaari.repository;

import com.kalaari.entity.db.DemandCenter;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandCenterRepository extends CrudRepository<DemandCenter, Long> {

    @Query(value = "select * from demand_center where ST_DWithin(cast(ST_Point(:request_lng, :request_lat) as geography), cast(ST_Point(lng, lat) as geography), radius) order by ST_Distance(cast(ST_Point(:request_lng, :request_lat) as geography), cast(ST_Point(lng, lat) as geography)) limit 1",
            nativeQuery = true)
    DemandCenter getNearestDemandCenter(@Param("request_lat") Double lat, @Param("request_lng") Double lng);

    DemandCenter findById(Long id);

    DemandCenter findByName(String name);

    List<DemandCenter> findAll();
}