package com.kalaari.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kalaari.entity.db.Vehicle;
import com.kalaari.exception.KalaariException;
import com.kalaari.service.CustomerService;
import com.kalaari.service.MatchmakingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/demand")
public class DemandController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MatchmakingService matchmakingService;

    @GetMapping(value = "/get_vehicles")
    @ResponseBody
    public List<Vehicle> getVehicles(@NotNull @RequestParam(value = "customer_id") Long customerId,
            @NotNull @RequestParam(value = "demand_center_id") Long demandCenterId,
            @NotNull @RequestParam(value = "customer_lat") Double customerLat,
            @NotNull @RequestParam(value = "customer_lng") Double customerLng,
            @NotNull @RequestParam(value = "time_of_request") Date timeOfRequest) throws KalaariException {
        return matchmakingService.getVehicles(customerId, customerLat, customerLng, demandCenterId, timeOfRequest);
    }
}