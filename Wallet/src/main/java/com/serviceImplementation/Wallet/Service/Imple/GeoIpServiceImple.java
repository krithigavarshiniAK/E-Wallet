package com.serviceImplementation.Wallet.Service.Imple;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.serviceImplementation.Wallet.Service.GeoIpService;
import com.serviceImplementation.Wallet.model.GeoIP;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

import static java.util.Objects.nonNull;

@Service
public class GeoIpServiceImple implements GeoIpService {
    @Autowired
    DatabaseReader databaseReader;
    @Override
    public GeoIP getIpLocation(String ip, HttpServletRequest httpServetRequest) throws IOException, GeoIp2Exception {
        GeoIP position = new GeoIP();
        String Location;

        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse cityResponse = databaseReader.city(ipAddress);
        if(nonNull(cityResponse) && nonNull(cityResponse.getCity())){
            String Continent = (cityResponse.getContinent() != null) ? cityResponse.getContinent().getName():" ";
            String Country = (cityResponse.getCountry() != null) ? cityResponse.getCountry().getName(): " ";

            Location = String.format("%s, %s, %s", Continent,Country,cityResponse.getCity().getName());
            position.setCity(cityResponse.getCity().getName());
            position.getIpAddress(ip);
//            position.getLongitude(cityResponse.getLocation() != null) ? cityResponse.getLocation().getLongitude() : 0;
//            position.getLatitude(cityResponse.getLocation() != null) ? cityResponse.getLocation().getLatitude() : 0;
        }
        return position;
    }
}
