package com.serviceImplementation.Wallet.WalletController;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.serviceImplementation.Wallet.Service.GeoIpService;
import com.serviceImplementation.Wallet.model.GeoIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    public GeoController(GeoIpService geoIpService) {
        this.geoIpService = geoIpService;
    }
    @GetMapping("/ip-location/{ipAddress}")
    public GeoIP getIpLocation(@PathVariable("ipAddress") String ipAddress) throws IOException, GeoIp2Exception {
        return geoIpService.getIpLocation(ipAddress);
    }
}

