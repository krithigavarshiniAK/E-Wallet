package com.serviceImplementation.Wallet.WalletController;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.serviceImplementation.Wallet.Service.GeoIpService;
import com.serviceImplementation.Wallet.model.GeoIP;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/V2/GeoLocation")
public class GeoController {

    @Autowired
    GeoIpService geoIpService;

    @GetMapping("/GeoMapping")
    public String Geo(){
        return "GeoLocation Mapping";
    }
    @GetMapping("/geoip/{ipAddress}")
    public GeoIP getGeoIp(@PathVariable String ipAddress,@PathVariable HttpServletRequest httpServletRequest)throws IOException, GeoIp2Exception {
        try {
            return geoIpService.getIpLocation(ipAddress, httpServletRequest);
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }

}
