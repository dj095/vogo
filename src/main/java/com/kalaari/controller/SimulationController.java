package com.kalaari.controller;

import com.kalaari.constant.EnumConstants;
import com.kalaari.entity.common.DemandCenterState;
import com.kalaari.entity.common.SimulationOutput;
import com.kalaari.entity.controller.InitialDataResponse;
import com.kalaari.exception.KalaariException;
import com.kalaari.service.SimulationDataGenerationService;
import com.kalaari.service.SimulationService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/simulation")
public class SimulationController {

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private SimulationDataGenerationService simulationDataGenerationService;

    @GetMapping(value = "/simulate")
    @ResponseBody
    public Map<EnumConstants.SimulationType, SimulationOutput> getVehicles() throws KalaariException {
        return simulationService.simulate();
    }

    @GetMapping(value = "/get_initial_state")
    @ResponseBody
    public List<DemandCenterState> getInitialState() {
        return simulationService.getInitialState();
    }

    @GetMapping(value = "/populate_data")
    @ResponseBody
    public String populateInitialData() {
        simulationDataGenerationService.populateInitialData();
        return "Success";
    }

    @GetMapping(value = "/get_initial_data")
    @ResponseBody
    public InitialDataResponse getInitialData() {
        return simulationDataGenerationService.getInitialData();
    }



}