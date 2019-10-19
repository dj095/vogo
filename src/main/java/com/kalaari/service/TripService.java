package com.kalaari.service;

import java.sql.Time;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.Trip;
import com.kalaari.repository.TripRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public List<Trip> getTripsAroundTime(Time time) {
        return tripRepository.findAllByStartTimeLessThanAndEndTimeGreaterThanOrderByNoOfTripsDesc(time, time);
    }
}