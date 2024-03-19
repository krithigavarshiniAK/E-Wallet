package com.serviceImplementation.Wallet.Service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.serviceImplementation.Wallet.model.GeoIP;

import java.io.IOException;

public interface GeoIpService {
    public GeoIP getIpLocation(String ipAddress)throws IOException, GeoIp2Exception;
}
