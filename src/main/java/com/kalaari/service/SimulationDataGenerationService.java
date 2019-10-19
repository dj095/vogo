package com.kalaari.service;

import com.kalaari.entity.controller.SimulationGeneratorRequest;
import com.kalaari.entity.db.Customer;
import com.kalaari.entity.db.CustomerLanePreference;
import com.kalaari.entity.db.DemandCenter;
import com.kalaari.model.SimulatorInput;
import com.kalaari.repository.CustomerLanePreferenceRepository;
import com.kalaari.repository.CustomerRepository;
import com.kalaari.repository.DemandCenterRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private SimulatorInput generateData(SimulationGeneratorRequest simulationGeneratorRequest) {
        List<SimulatorInput.SimulatorInputEntity> simulatorInputEntityList = new ArrayList<>();
        Map<Long, Long> dcIdToCountMap = simulationGeneratorRequest.getData().stream().collect(Collectors.toMap(
                SimulationGeneratorRequest.SimulationGeneratorRequestEntity::getDcId,
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
                Integer toIndex = (generateRandomNumber(numDc - 1)) % numDc;
                Long toDcId = dcIdList.get(toIndex);

                Customer customer = new Customer(customerId.toString(), 3D, demandCenter.getLat(), demandCenter.getLng(), 0);
                customer = customerRepository.save(customer);
                CustomerLanePreference customerLanePreference = new CustomerLanePreference(customer.getId(), dcId, toDcId, 0);
                customerLanePreferenceRepository.save(customerLanePreference);

                simulatorInputEntityList.add(new SimulatorInput.SimulatorInputEntity(dcId, toDcId, customer.getId()));
                dcIdToCountMap.put(dcId, dcIdToCountMap.get(dcId)-1);
            }
            else {
                dcIdToCountMap.remove(dcId);
            }
        }

        return new SimulatorInput(simulatorInputEntityList);
    }

    private Integer generateRandomNumber(Integer maxval) {
        return (int) (Math.random() * maxval);
    }
}
