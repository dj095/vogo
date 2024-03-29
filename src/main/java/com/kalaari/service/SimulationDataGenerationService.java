package com.kalaari.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalaari.entity.controller.InitialDataResponse;
import com.kalaari.entity.controller.SimulationGeneratorRequest;
import com.kalaari.entity.db.Customer;
import com.kalaari.entity.db.CustomerLanePreference;
import com.kalaari.entity.db.DemandCenter;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.entity.db.DemandLanePrediction;
import com.kalaari.entity.db.VehicleLocation;
import com.kalaari.model.SimulatorInput;
import com.kalaari.repository.CustomerLanePreferenceRepository;
import com.kalaari.repository.CustomerRepository;
import com.kalaari.repository.DemandCenterPredictionRepository;
import com.kalaari.repository.DemandCenterRepository;
import com.kalaari.repository.DemandLanePredictionRepository;
import com.kalaari.repository.GenericRepository;
import com.kalaari.repository.VehicleLocationRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimulationDataGenerationService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DemandCenterRepository demandCenterRepository;

    @Autowired
    private CustomerLanePreferenceRepository customerLanePreferenceRepository;

    @Autowired
    private DemandCenterPredictionRepository demandCenterPredictionRepository;

    @Autowired
    private VehicleLocationRepository vehicleLocationRepository;

    @Autowired
    private DemandLanePredictionRepository demandLanePredictionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenericRepository genericRepository;

    public SimulatorInput generateData() {
        SimulationGeneratorRequest simulationGeneratorRequest = mockRequest();
        createDemandCentreData(simulationGeneratorRequest);
        List<SimulatorInput.SimulatorInputEntity> simulatorInputEntityList = new ArrayList<>();
        Map<Long, Long> dcIdToCountMap = simulationGeneratorRequest.getData().stream()
                .collect(Collectors.toMap(SimulationGeneratorRequest.SimulationGeneratorRequestEntity::getDcId,
                        SimulationGeneratorRequest.SimulationGeneratorRequestEntity::getCount));
        List<Long> dcIdList = new ArrayList<>(dcIdToCountMap.keySet());
        Integer numDc = dcIdList.size();
        Long customerId = 1L;
        while (!dcIdToCountMap.isEmpty()) {
            Integer randomIndex = (generateRandomNumber(numDc)) % numDc;
            Long dcId = dcIdList.get(randomIndex);
            DemandCenter demandCenter = demandCenterRepository.findById(dcId);
            if (!dcIdToCountMap.containsKey(dcId))
                continue;
            if (dcIdToCountMap.get(dcId) > 0) {
                Integer toIndex = (randomIndex + 1 + (generateRandomNumber(numDc - 1))) % numDc;
                Long toDcId = dcIdList.get(toIndex);

                Customer customer = new Customer(customerId.toString(), 3D, demandCenter.getLat(),
                        demandCenter.getLng(), (int)(Math.random()*10));
                customer = customerRepository.save(customer);
                CustomerLanePreference customerLanePreference = new CustomerLanePreference(customer.getId(), dcId,
                        toDcId, 0);
                customerLanePreferenceRepository.save(customerLanePreference);

                simulatorInputEntityList.add(new SimulatorInput.SimulatorInputEntity(dcId, toDcId, customer.getId()));
                dcIdToCountMap.put(dcId, dcIdToCountMap.get(dcId) - 1);
            } else {
                dcIdToCountMap.remove(dcId);
            }
        }
        try {
            log.debug(objectMapper.writeValueAsString(simulatorInputEntityList));
        } catch (Exception ex) {
            log.error("Error in generating data: {}", ex.getMessage(), ex);
        }
        return new SimulatorInput(simulatorInputEntityList);
    }

    private Integer generateRandomNumber(Integer maxval) {
        return (int) (Math.random() * maxval);
    }

    public void populateInitialData() {
        SimulationGeneratorRequest simulationGeneratorRequest = mockRequest();
        createDemandCentreData(simulationGeneratorRequest);
    }
    private void createDemandCentreData(SimulationGeneratorRequest simulationGeneratorRequest) {
        List<DemandCenter> demandCenterList = new ArrayList<>();
        Map<Long, DemandCenter> demandCenterMap = new HashMap<>();
        demandCenterMap.put(1L, new DemandCenter("Whitefield", 12.977720, 77.741395, null, 200.0));
        demandCenterMap.put(2L, new DemandCenter("Marathahalli", 12.967636, 77.695034, null, 200.0));
        demandCenterMap.put(3L, new DemandCenter("Sarjapur", 12.859945, 77.791261, null, 200.0));
//        demandCenterMap.put(4L, new DemandCenter("Electronic City", 12.834977, 77.665627, null, 200.0));
        for (SimulationGeneratorRequest.SimulationGeneratorRequestEntity simulationGeneratorRequestEntity : simulationGeneratorRequest.getData()) {
            DemandCenter demandCenter = demandCenterMap.get(simulationGeneratorRequestEntity.getDcId());
            demandCenter.setNoOfVehicles(simulationGeneratorRequestEntity.getSupplyCount());
            createVehicles(demandCenter);
            DemandCenter demandCenterExisting = demandCenterRepository.findByName(demandCenter.getName());
            if (demandCenterExisting == null) {
                demandCenter = demandCenterRepository.save(demandCenter);
            }
            demandCenterList.add(demandCenter);
            createDemandCentrePredictionData(demandCenter);
        }
        createLanePredictionData(demandCenterList);
    }

    private void createVehicles(DemandCenter demandCenter) {
        for (int i = 0 ; i < demandCenter.getNoOfVehicles() ; i++) {
            String registrationNumber = "KA" + demandCenter.getName().toUpperCase().substring(0, 4) + String.valueOf(i+1);
            VehicleLocation vehicleLocation = vehicleLocationRepository.findByVehicleNumber(registrationNumber);
            if (vehicleLocation != null)
                continue;
            vehicleLocation = new VehicleLocation(registrationNumber, new Date(), demandCenter.getLat(), demandCenter.getLng(), null);
            vehicleLocationRepository.save(vehicleLocation);
        }
    }

    private void createDemandCentrePredictionData(DemandCenter demandCenter) {
        Map<String, Long> idleWaitMinsMap = new HashMap<>();
        idleWaitMinsMap.put("Whitefield", 5L);
        idleWaitMinsMap.put("Marathahalli", 1L);
        idleWaitMinsMap.put("Sarjapur", 8L);
        idleWaitMinsMap.put("Electronic City", 60L);
        DemandCenterPrediction demandCenterPrediction = demandCenterPredictionRepository.findByDemandCenterIdOrderByIdleWaitMins(demandCenter.getId());
        if (demandCenterPrediction != null)
            return;
        demandCenterPrediction = new DemandCenterPrediction(demandCenter.getId(),
                null, null, idleWaitMinsMap.get(demandCenter.getName()));
        demandCenterPredictionRepository.save(demandCenterPrediction);
    }

    private void createLanePredictionData(List<DemandCenter> demandCenterList) {

        Map<String, Map<String, Long>> estimatedDemandMap = new HashMap<>();

        estimatedDemandMap.put("Whitefield", new HashMap<>());
        estimatedDemandMap.get("Whitefield").put("Marathahalli", 8L);
        estimatedDemandMap.get("Whitefield").put("Sarjapur", 2L);
        estimatedDemandMap.get("Whitefield").put("Electronic City", 4L);

        estimatedDemandMap.put("Marathahalli", new HashMap<>());
        estimatedDemandMap.get("Marathahalli").put("Whitefield", 4L);
        estimatedDemandMap.get("Marathahalli").put("Sarjapur", 3L);
        estimatedDemandMap.get("Marathahalli").put("Electronic City", 4L);

        estimatedDemandMap.put("Sarjapur", new HashMap<>());
        estimatedDemandMap.get("Sarjapur").put("Whitefield", 2L);
        estimatedDemandMap.get("Sarjapur").put("Marathahalli", 6L);
        estimatedDemandMap.get("Sarjapur").put("Electronic City", 4L);

        estimatedDemandMap.put("Electronic City", new HashMap<>());
        estimatedDemandMap.get("Electronic City").put("Whitefield", 1L);
        estimatedDemandMap.get("Electronic City").put("Marathahalli", 2L);
        estimatedDemandMap.get("Electronic City").put("Sarjapur", 3L);

        for (DemandCenter fromDemandCenter : demandCenterList) {
            for (DemandCenter toDemandCenter : demandCenterList) {
                if (fromDemandCenter.equals(toDemandCenter))
                    continue;
                DemandLanePrediction demandLanePrediction = demandLanePredictionRepository
                        .findTopByFromDemandCenterIdAndToDemandCenterIdOrderByEstimatedDemandDesc(fromDemandCenter.getId(), toDemandCenter.getId());
                if (demandLanePrediction != null)
                    continue;
                demandLanePrediction = new DemandLanePrediction(fromDemandCenter.getId(),
                        toDemandCenter.getId(), null, null, estimatedDemandMap.get(fromDemandCenter.getName()).get(toDemandCenter.getName()),
                        null);
                demandLanePredictionRepository.save(demandLanePrediction);
            }
        }
    }

    public void setRequestString(String json) {
        requestString = json;
    }

    private String requestString;

    private SimulationGeneratorRequest mockRequest() {
//        String requestString = "{\"data\": [{\"dc_id\": 1,\"count\": 10,\"supply_count\": 12},{\"dc_id\": 2,\"count\": 5,\"supply_count\": 7},{\"dc_id\": 3,\"count\": 7,\"supply_count\": 10},{\"dc_id\": 4,\"count\": 3,\"supply_count\": 5}]}";
        requestString = "{\"data\": [{\"dc_id\": 1,\"count\": 10,\"supply_count\": 4},{\"dc_id\": 2,\"count\": 5,\"supply_count\": 2},{\"dc_id\": 3,\"count\": 4,\"supply_count\": 8}]}";
        try {
            return objectMapper.readValue(requestString, SimulationGeneratorRequest.class);
        } catch (Exception ex) {
            return null;
        }
    }

    public InitialDataResponse getInitialData() {
        List<DemandCenter> demandCentres = demandCenterRepository.findAll();
        InitialDataResponse initialDataResponse = new InitialDataResponse();
        Set<InitialDataResponse.InitialDataResponseEntity> data = new HashSet<>();
        for (DemandCenter demandCenter : demandCentres) {
            DemandCenterPrediction demandCenterPrediction = demandCenterPredictionRepository.findByDemandCenterIdOrderByIdleWaitMins(demandCenter.getId());
            data.add(new InitialDataResponse.InitialDataResponseEntity(demandCenter.getId(), demandCenter.getName(), demandCenter.getNoOfVehicles(), demandCenterPrediction.getIdleWaitMins()));
        }
        initialDataResponse.setData(new ArrayList<>(data));
        return initialDataResponse;
    }
}
