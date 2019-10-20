package com.kalaari.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.constant.EnumConstants;
import com.kalaari.entity.common.DemandCenterState;
import com.kalaari.entity.common.SimulationOutput;
import com.kalaari.entity.common.Trip;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.exception.KalaariException;
import com.kalaari.model.FCFSOutput;
import com.kalaari.model.SimulatorInput;
import com.kalaari.simulator.SupplyVisibilitySimulator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimulationService {

    @Autowired
    private SupplyVisibilitySimulator supplyVisibilitySimulator;

    @Autowired
    private FCFSSimulatorService fcfsSimulatorService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DemandCenterService demandCenterService;

    @Autowired
    private SimulationDataGenerationService simulationDataGenerationService;

    public Map<EnumConstants.SimulationType, List<Double>> metricGenerator() throws KalaariException {

        Map<EnumConstants.SimulationType, List<Double>> output = new HashMap<>();

        for (int i = 0; i < 100; i++) {
            Map<EnumConstants.SimulationType, SimulationOutput> ithOutput = simulate();

            SimulationOutput fcfsSimulationOutput = ithOutput.get(EnumConstants.SimulationType.FCFS);
            SimulationOutput svSimulationOutput = ithOutput.get(EnumConstants.SimulationType.SV);

            Double fcfsWait = getWAITForOutput(fcfsSimulationOutput);
            Double svWait = getWAITForOutput(svSimulationOutput);

            output.putIfAbsent(EnumConstants.SimulationType.FCFS, new ArrayList<>());
            output.get(EnumConstants.SimulationType.FCFS).add(fcfsWait);

            output.putIfAbsent(EnumConstants.SimulationType.SV, new ArrayList<>());
            output.get(EnumConstants.SimulationType.SV).add(svWait);
            log.info("i: {}", i);
        }

        return output;
    }

    private Double getWAITForOutput(SimulationOutput simulationOutput) {

        Double wait = 0D;
        int totalTrucks = 0;
        for (DemandCenterState demandCenterState : simulationOutput.getDemandCenterStates()) {
            wait += (double) demandCenterState.getIdleTimeMins() * (double) demandCenterState.getNoOfVehicles();
            totalTrucks += demandCenterState.getNoOfVehicles();
        }

        wait = wait / totalTrucks;

        return wait;
    }

    public Map<EnumConstants.SimulationType, SimulationOutput> simulate() throws KalaariException {

        Map<EnumConstants.SimulationType, SimulationOutput> simulationOutputMap = new HashMap<>();

        // BUILD SIMULATOR INPUT
        SimulatorInput simulatorInput = simulationDataGenerationService.generateData();

        supplyVisibilitySimulator.reset();

        // SIMULATE USING SV SIMULATOR
        SimulationOutput svSimulationOutput = supplyVisibilitySimulator.simulate(simulatorInput);
        supplyVisibilitySimulator.reset();

        // SIMULATE USING FCFS SIMULATOR
        SimulationOutput fcfsSimilationOutput = buildSimulatorOutput(fcfsSimulatorService.runFCFSSimulator(
                simulatorInput, supplyVisibilitySimulator.getDemandCenterIdToSupply()), simulatorInput);
        supplyVisibilitySimulator.reset();

        simulationOutputMap.put(EnumConstants.SimulationType.SV, svSimulationOutput);
        simulationOutputMap.put(EnumConstants.SimulationType.FCFS, fcfsSimilationOutput);
        return simulationOutputMap;
    }

    private SimulationOutput buildSimulatorOutput(List<FCFSOutput> fcfsOutputs, SimulatorInput simulatorInput) {
        SimulationOutput simulationOutput = new SimulationOutput();
        simulationOutput.setTrips(new ArrayList<>());
        simulationOutput.setDemandCenterStates(new ArrayList<>());

        for (FCFSOutput fcfsOutput : fcfsOutputs) {
            Long stationId = fcfsOutput.getStationId();
            Long count = fcfsOutput.getCount();
            DemandCenterPrediction demandCenterPrediction = demandCenterService
                    .getDemandCenterPredictionById(stationId);
            simulationOutput.getDemandCenterStates()
                    .add(new DemandCenterState(stationId, count, demandCenterPrediction.getIdleWaitMins()));
        }

        for (SimulatorInput.SimulatorInputEntity input : simulatorInput.getData()) {
            simulationOutput.getTrips().add(new Trip(input.getFromStation(), input.getToStation(), true));
        }
        return simulationOutput;
    }

    public List<DemandCenterState> getInitialState() {
        List<DemandCenterState> demandCenterStates = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : supplyVisibilitySimulator.getDemandCenterIdToSupply().entrySet()) {
            DemandCenterPrediction demandCenterPrediction = demandCenterService
                    .getDemandCenterPredictionById(entry.getKey());
            demandCenterStates.add(
                    new DemandCenterState(entry.getKey(), entry.getValue(), demandCenterPrediction.getIdleWaitMins()));
        }
        return demandCenterStates;
    }
}