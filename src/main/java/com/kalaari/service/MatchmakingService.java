package com.kalaari.service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.kalaari.entity.db.Customer;
import com.kalaari.entity.db.CustomerLanePreference;
import com.kalaari.entity.db.DemandCenter;
import com.kalaari.entity.db.DemandCenterPrediction;
import com.kalaari.entity.db.DemandLanePrediction;
import com.kalaari.entity.db.VehicleLocation;
import com.kalaari.exception.KalaariErrorCode;
import com.kalaari.exception.KalaariException;
import com.kalaari.util.GeoLocationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchmakingService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DemandCenterService demandCenterService;

    @Autowired
    private VehicleService vehicleService;

    public List<VehicleLocation> getVehicles(Long customerId, Date timeOfRequest) throws KalaariException {

        // FIND CUSTOMER
        Customer customer = customerService.getCustomerById(customerId);
        log.info("customer: {}", customer);
        if (customer == null) {
            throw new KalaariException(KalaariErrorCode.BAD_REQUEST, "Customer: " + customerId + " does not exist !");
        }

        Double customerLat = customer.getLat();
        Double customerLng = customer.getLng();

        // FIND NEAREST DEMAND CENTER
        DemandCenter nearestDemandCenter = demandCenterService.getNearestDemandCenter(customerLat, customerLng);
        log.info("nearestDemandCenter: {}", nearestDemandCenter);

        if (nearestDemandCenter == null) {
            throw new KalaariException(KalaariErrorCode.BAD_REQUEST, "No Nearest Demand Center Found !");
        }

        // GET VEHICLES TO ALLOCATE
        List<VehicleLocation> vehiclesToAllocate = vehicleService.getAllNearbyVehiclesAroundTime(customerLat,
                customerLng, timeOfRequest);
        log.info("vehiclesToAllocate size: {}", vehiclesToAllocate);
        if (CollectionUtils.isEmpty(vehiclesToAllocate)) {
            throw new KalaariException(KalaariErrorCode.BAD_REQUEST, "No Vehices were found to allocate around time: "
                    + timeOfRequest + ", for lat: " + customerLat + " lng: " + customerLng);
        }

        // GET CUSTOMER LANE PREF
        List<CustomerLanePreference> lanePreferences = customerService.getCustomerLanePreferences(customerId,
                nearestDemandCenter.getId());
        log.info("lanePreferences size: {}", lanePreferences);

        // GET CENTER PREDICTIONS AROUND TIME OF REQUEST
        List<DemandCenterPrediction> demandCenterPredictions = demandCenterService
                .getTop10DemandCenterPredictions(new Time(timeOfRequest.getTime()));
        log.info("demandCenterPredictions size: {}", demandCenterPredictions.size());

        // ALLOCATE
        return shouldShowVehicles(customer, lanePreferences, demandCenterPredictions, vehiclesToAllocate,
                nearestDemandCenter, timeOfRequest) ? vehiclesToAllocate : new ArrayList<>();
    }

    public boolean shouldShowVehicles(Customer customer, List<CustomerLanePreference> lanePreferences,
            List<DemandCenterPrediction> demandCenterPredictions, List<VehicleLocation> vehiclesToAllocate,
            DemandCenter sourceDC, Date timeOfRequest) {

        boolean show = matchLanePrefWithDemandPrediction(lanePreferences, demandCenterPredictions);
        if (!show) {

            show = customer.isNewCustomer();
            if (!show) {

                if (!CollectionUtils.isEmpty(demandCenterPredictions)) {
                    for (DemandCenterPrediction destinationDCP : demandCenterPredictions) {

                        // GET LANE PREDICTIONS AROUND TIME OF REQUEST
                        List<DemandLanePrediction> demandLanePredictions = demandCenterService.getLanePredictions(
                                sourceDC.getId(), destinationDCP.getDemandCenterId(),
                                new Time(timeOfRequest.getTime()));
                        log.info("demandLanePredictions size: {}", demandLanePredictions.size());

                        if (!CollectionUtils.isEmpty(demandLanePredictions)) {

                            for (DemandLanePrediction dlp : demandLanePredictions) {
                                if (vehiclesToAllocate.size() >= dlp.getEstimatedDemand()) {

                                    // SUPPLY WILL BE EXCESS EVEN AFTER SATISFYING DEST DEMAND
                                    show = true;
                                    break;
                                }
                            }
                        }

                        if (show) {
                            break;
                        }
                    }
                }

                if (!show) {

                    double distanceBetweenCustomerAndSourceDC = GeoLocationUtils.distance(customer.getLat(),
                            customer.getLng(), sourceDC.getLat(), sourceDC.getLng());
                    show = distanceBetweenCustomerAndSourceDC < 50000D;
                }
            }
        }
        return show;
    }

    public boolean matchLanePrefWithDemandPrediction(List<CustomerLanePreference> lanePreferences,
            List<DemandCenterPrediction> demandCenterPredictions) {
        for (CustomerLanePreference lanePreference : lanePreferences) {
            for (DemandCenterPrediction demandCenterPrediction : demandCenterPredictions) {
                if (lanePreference.getToDemandCenter().equals(demandCenterPrediction.getDemandCenterId())) {
                    return true;
                }
            }
        }
        return false;
    }
}