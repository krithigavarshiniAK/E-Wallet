package com.serviceImplementation.Wallet.Service.Imple;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.serviceImplementation.Wallet.Service.GeoIpService;
import com.serviceImplementation.Wallet.model.GeoIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeoIpServiceImple implements GeoIpService {

    @Autowired
    DatabaseReader databaseReader;

    @Autowired
    public GeoIpServiceImple(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public GeoIP getIpLocation(String ipAddress) throws IOException, GeoIp2Exception {
        InetAddress ip = InetAddress.getByName(ipAddress);
        CityResponse cityResponse = databaseReader.city(ip);

        GeoIP geoIP = new GeoIP();
        geoIP.setIpAddress(ipAddress);
        geoIP.setCity(cityResponse.getCity().getName());
        geoIP.setCountry(cityResponse.getCountry().getName());
        geoIP.setLatitude(String.valueOf(cityResponse.getLocation().getLatitude()));
        geoIP.setLongitude(String.valueOf(cityResponse.getLocation().getLongitude()));

        return geoIP;
    }
}
