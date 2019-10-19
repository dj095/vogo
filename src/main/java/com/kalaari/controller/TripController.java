package com.kalaari.controller;

import java.sql.Time;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kalaari.entity.db.Trip;
import com.kalaari.exception.KalaariException;
import com.kalaari.service.TripService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping(value = "/get_trips_around_time")
    @ResponseBody
    public List<Trip> getTripsAroundTime(@NotNull @RequestParam(value = "time") Time time) throws KalaariException {
        return tripService.getTripsAroundTime(time);
    }
}