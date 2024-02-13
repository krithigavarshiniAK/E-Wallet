package com.serviceImplementation.Wallet.WalletController;

import com.serviceImplementation.Wallet.Api_Interaction.ApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/interaction")
public class ApiController {

    @Autowired
    private ApiCaller apiCaller;

    @GetMapping("/callApi")
    public String callApi(@RequestParam String apiUrl,
                          @RequestParam String username,
                          @RequestParam String password) {
        try {
            return apiCaller.makeApiCall(apiUrl, username, password);
        } catch (IOException e) {
            return "Error making API call: " + e.getMessage();
        }
    }

}
