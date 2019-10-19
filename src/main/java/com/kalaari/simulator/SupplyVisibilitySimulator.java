package com.kalaari.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kalaari.entity.common.DemandCenterState;
import com.kalaari.entity.common.SimulationOutput;
import com.kalaari.entity.common.Trip;
import com.kalaari.entity.db.DemandCenter;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.entity.db.VehicleLocation;
import com.kalaari.exception.KalaariException;
import com.kalaari.model.SimulatorInput;
import com.kalaari.service.DemandCenterService;
import com.kalaari.service.MatchmakingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SupplyVisibilitySimulator {

    @Autowired
    private MatchmakingService matchmakingService;
    @Autowired
    private DemandCenterService demandCenterService;

    private Map<Long, Long> demandCenterIdToSupply;

    @PostConstruct
    public void postConstruct() {
        reset();
    }

    public SimulationOutput simulate(SimulatorInput simulatorInput, Long customerId) throws KalaariException {
        SimulationOutput simulationOutput = new SimulationOutput();

        List<Trip> trips = new ArrayList<>();
        for (SimulatorInput.SimulatorInputEntity input : simulatorInput.getData()) {
            Long fromDCId = input.getFromStation();
            Long toDCId = input.getToStation();
            List<VehicleLocation> allocatedVehicles = matchmakingService.findVehicles(customerId);
            if (allocatedVehicles.size() > 0) {

                // WE PREVENTED TRIP FROM HAPPENING
                trips.add(new Trip(fromDCId, toDCId, false));
            } else {

                // WE ALLOWED THE TRIP
                trips.add(new Trip(fromDCId, toDCId, true));
                demandCenterIdToSupply.put(fromDCId, demandCenterIdToSupply.get(fromDCId) - 1);
                demandCenterIdToSupply.put(toDCId, demandCenterIdToSupply.get(toDCId) + 1);
            }
        }

        List<DemandCenterState> demandCenterStates = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : demandCenterIdToSupply.entrySet()) {
            Long dcId = entry.getKey();
            Long noOfVehicles = entry.getValue();
            DemandCenterPrediction demandCenterPrediction = demandCenterService.getDemandCenterPredictionById(dcId);
            demandCenterStates.add(new DemandCenterState(dcId, noOfVehicles, demandCenterPrediction.getIdleWaitMins()));
        }

        simulationOutput.setTrips(trips);
        simulationOutput.setDemandCenterStates(demandCenterStates);
        return simulationOutput;
    }

    public void reset() {
        demandCenterIdToSupply = new HashMap<>();
        List<DemandCenter> demandCenters = demandCenterService.getAllDemandCenters();
        for (DemandCenter demandCenter : demandCenters) {
            demandCenterIdToSupply.put(demandCenter.getId(), demandCenter.getNoOfVehicles());
        }
    }

    public Map<Long, Long> getDemandCenterIdToSupply() {
        return this.demandCenterIdToSupply;
    }
}