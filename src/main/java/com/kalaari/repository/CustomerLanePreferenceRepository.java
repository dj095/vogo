package com.kalaari.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.CustomerLanePreference;

@Repository
public interface CustomerLanePreferenceRepository extends CrudRepository<CustomerLanePreference, Long> {

    List<CustomerLanePreference> findAllByCustomerIdAndFromDemandCenterOrderByWeight(Long customerId, Long fromDcId);
}