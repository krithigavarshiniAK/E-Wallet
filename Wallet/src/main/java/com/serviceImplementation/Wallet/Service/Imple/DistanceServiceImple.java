package com.serviceImplementation.Wallet.Service.Imple;

import com.serviceImplementation.Wallet.Service.DistanceService;
import com.serviceImplementation.Wallet.model.Coordinates;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceImple implements DistanceService {

    private static final double EARTH_RADIUS = 6371;
    public double calculateDistance(Coordinates coordinates) {

        double lat1Rad = Math.toRadians(coordinates.getLatitude1());
        double lon1Rad = Math.toRadians(coordinates.getLongitude1());
        double lat2Rad = Math.toRadians(coordinates.getLatitude2());
        double lon2Rad = Math.toRadians(coordinates.getLongitude2());

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
