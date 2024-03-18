package com.serviceImplementation.Wallet.Service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.serviceImplementation.Wallet.model.GeoIP;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface GeoIpService {
    public GeoIP getIpLocation(String ip, HttpServletRequest httpServetRequest)throws IOException, GeoIp2Exception;

}
