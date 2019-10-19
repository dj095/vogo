package com.kalaari.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalaari.entity.db.Customer;
import com.kalaari.entity.db.CustomerLanePreference;
import com.kalaari.repository.CustomerLanePreferenceRepository;
import com.kalaari.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerLanePreferenceRepository customerLanePreferenceRepository;

    public List<CustomerLanePreference> getCustomerLanePreferences(Long customerId, Long demandCenterId) {
        return customerLanePreferenceRepository.findAllByCustomerIdAndFromDemandCenterOrderByWeight(customerId,
                demandCenterId);
    }

    public Customer getCustomerById() {
        return null;
    }
}