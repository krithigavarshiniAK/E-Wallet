package com.serviceImplementation.Wallet.Api_Interaction;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiCaller {

    public String makeApiCall(String apiUrl, String username, String password) throws IOException {
        // Create a CredentialsProvider and set the provided username and password
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        // Create a CloseableHttpClient with basic authentication
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        // Create an HTTP GET request with the provided API URL
        HttpGet httpGet = new HttpGet(apiUrl);

        // Execute the HTTP request and obtain the response
        HttpResponse response = httpClient.execute(httpGet);

        // Get the status code of the response
        int statusCode = response.getStatusLine().getStatusCode();

        // Check if the response is successful (status code 200)
        if (statusCode == 200) {
            // If successful, read and return the response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder responseContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
            return responseContent.toString();
        } else {
            // If unsuccessful, return an error message with the response code
            return "API request failed. Response Code: " + statusCode;
        }
    }

}
