package com.serviceImplementation.Wallet.WalletController;

import com.serviceImplementation.Wallet.Service.DistanceService;
import com.serviceImplementation.Wallet.model.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/Coordinates")
public class CoordinatesController {
    @Autowired
    DistanceService distanceService;

    @GetMapping("/coord-test")
    public String coord(){
        return "Coordinates";
    }
    @PostMapping("/distance")
    public double calculateDistance(@RequestBody Coordinates coordinates){
        return distanceService.calculateDistance(coordinates);
    }
}
