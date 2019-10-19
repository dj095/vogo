package com.kalaari.util;

import static java.lang.Math.toRadians;

public class GeoLocationUtils {

    private static final int EARTH_RADIUS = 6371; // APPROX EARTH RADIUS IN KM

    public static double distance(double startLat, double startLong, double endLat, double endLong) {

        double dLat = toRadians((endLat - startLat));
        double dLong = toRadians((endLong - startLong));

        startLat = toRadians(startLat);
        endLat = toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}