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

import com.kalaari.entity.db.VehicleLocation;
import com.kalaari.service.VehicleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping(value = "/get_vehicles_around_time")
    @ResponseBody
    public List<VehicleLocation> getVehiclesAroundTime(@NotNull @RequestParam(value = "time") Time time) {
        return vehicleService.getVehiclesAroundTime(time);
    }
}