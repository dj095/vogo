package com.kalaari.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.DemandCenter;

@Repository
public interface DemandCenterRepository extends CrudRepository<DemandCenter, Long> {
}