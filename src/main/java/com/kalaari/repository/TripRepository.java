package com.kalaari.repository;

import java.sql.Time;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.Trip;

@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {

    List<Trip> findAllByStartTimeLessThanAndEndTimeGreaterThanOrderByNoOfTripsDesc(Time startTime, Time endTime);
}