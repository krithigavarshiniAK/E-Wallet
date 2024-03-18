package com.serviceImplementation.Wallet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class GeoIP {
    private String ipAddress;
    private String city;
    private String latitude;
    private String longitude;

    public String getIpAddress(String ip) {
        return ipAddress;
    }

    public String getLatitude(String b) {
        return b;
    }
}
