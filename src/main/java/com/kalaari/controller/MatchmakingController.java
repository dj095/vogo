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

import com.kalaari.entity.db.VehicleLocation;
import com.kalaari.exception.KalaariException;
import com.kalaari.service.MatchmakingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/matchmaking")
public class MatchmakingController {

    @Autowired
    private MatchmakingService matchmakingService;

    @GetMapping(value = "/get_vehicles")
    @ResponseBody
    public List<VehicleLocation> getVehicles(@NotNull @RequestParam(value = "customer_id") Long customerId,
            @NotNull @RequestParam(value = "time_of_request") Date timeOfRequest) throws KalaariException {
        return matchmakingService.getVehicles(customerId, timeOfRequest);
    }
}