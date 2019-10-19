package com.kalaari.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kalaari.model.FCFSOutput;
import com.kalaari.model.SimulatorInput;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FCFSSimulatorService {

    public List<FCFSOutput> runFCFSSimulator(SimulatorInput simulatorInput, Map<Long, Long> initialSupply) {
        List<FCFSOutput> outputList = new ArrayList<>();
        List<SimulatorInput.SimulatorInputEntity> simulatorInputEntityList = simulatorInput.getData();
        Map<Long, Long> outputMap = new HashMap<>();

        for (SimulatorInput.SimulatorInputEntity simulatorInputEntity : simulatorInputEntityList) {
            outputMap.putIfAbsent(simulatorInputEntity.getFromStation(),
                    initialSupply.get(simulatorInputEntity.getFromStation()));
            if (outputMap.containsKey(simulatorInputEntity.getFromStation())
                    && outputMap.get(simulatorInputEntity.getFromStation()) > 0) {
                outputMap.put(simulatorInputEntity.getFromStation(),
                        outputMap.get(simulatorInputEntity.getFromStation()) - 1);
            }
        }

        for (Map.Entry<Long, Long> entry : outputMap.entrySet()) {
            outputList.add(new FCFSOutput(entry.getKey(), entry.getValue()));
        }

        return outputList;
    }
}
