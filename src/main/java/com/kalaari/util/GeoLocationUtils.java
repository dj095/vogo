package com.kalaari.util;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import com.google.maps.model.LatLng;

import javafx.util.Pair;

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

    /**
     * @param latitude
     *            ENTRY POINT COORDINATES
     * @param longitude
     *            DISTANCE THAT YOU WANT TO MOVE THE POINT BY
     * @param distanceInMetres
     *            AN ANGLE, DIRECTION TOWARDS WHICH YOU WANT TO MOVE THE POINT. 0 IS
     *            TOWARDS THE NORTH, 90 - EAST, 180 - SOUTH, 270 - WEST. AND ALL
     *            BETWEEN, I.E. 45 IS NORTH EAST.
     * @param bearing
     *            EARTH RADIUS IN METRES
     * @return
     */
    public static Pair<Double, Double> getGeopointSomeDistanceFromGivenPoint(double latitude, double longitude,
            double distanceInMetres, double bearing) {
        double brngRad = toRadians(bearing);
        double latRad = toRadians(latitude);
        double lonRad = toRadians(longitude);
        int earthRadiusInMetres = EARTH_RADIUS * 1000;
        double distFrac = distanceInMetres / earthRadiusInMetres;

        double latitudeResult = asin(sin(latRad) * cos(distFrac) + cos(latRad) * sin(distFrac) * cos(brngRad));
        double a = atan2(sin(brngRad) * sin(distFrac) * cos(latRad), cos(distFrac) - sin(latRad) * sin(latitudeResult));
        double longitudeResult = (lonRad + a + 3 * PI) % (2 * PI) - PI;
        return new Pair<>(toDegrees(latitudeResult), toDegrees(longitudeResult));
    }

    public static Pair<LatLng, LatLng> getBoundingBoxFromCircularGeofence(Double latitude, Double longitude,
            Double radiusMeters) {
        double longitudeD = (Math.asin((radiusMeters / 1000) / (EARTH_RADIUS * Math.cos(Math.PI * latitude / 180))))
                * 180 / Math.PI;
        double latitudeD = (Math.asin((radiusMeters / 1000) / (double) EARTH_RADIUS)) * 180 / Math.PI;
        double latitudeMax = latitude + (latitudeD);
        double latitudeMin = latitude - (latitudeD);
        double longitudeMax = longitude + (longitudeD);
        double longitudeMin = longitude - (longitudeD);
        return new Pair<>(new LatLng(latitudeMin, longitudeMin), new LatLng(latitudeMax, longitudeMax));
    }
}