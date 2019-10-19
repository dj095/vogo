package com.kalaari.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kalaari.entity.db.DemandCenter;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.entity.db.DemandLanePrediction;
import com.kalaari.service.DemandCenterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/demand_center")
public class DemandCenterController {

    @Autowired
    private DemandCenterService demandCenterService;

    @GetMapping(value = "/get")
    @ResponseBody
    public List<DemandCenter> getAllDemandCenters() {
        return demandCenterService.getAllDemandCenters();
    }

    @GetMapping(value = "/get_center_prediction_around_time")
    @ResponseBody
    public List<DemandCenterPrediction> getDemandCenterPredictionAroundTime() {
        return demandCenterService.getDemandCenterPredictionAroundTime();
    }

    @GetMapping(value = "/get_lane_prediction_around_time")
    @ResponseBody
    public List<DemandLanePrediction> getDemandLanePredictionAroundTime() {
        return demandCenterService.getDemandLanePredictionAroundTime();
    }
}