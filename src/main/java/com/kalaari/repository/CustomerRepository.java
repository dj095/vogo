package com.kalaari.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kalaari.entity.db.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}