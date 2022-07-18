package mapservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import pl.adampolsa.mapservice.msg.request.GeoCodingDistanceRequest;
import pl.adampolsa.mapservice.msg.request.GeoCodingLocRequest;
import pl.adampolsa.mapservice.msg.response.GeocodingDistanceResponse;
import pl.adampolsa.mapservice.msg.response.GeocodingLocRespEntry;
import pl.adampolsa.mapservice.msg.response.GeocodingLocResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class DistanceAnalyzer {


    public GeocodingLocResponse getResponse(String address) {
        GeocodingLocResponse locResponse;
        Gson gson = new Gson();
        try {
            URL url = new URL("http://10.10.10.83:8080/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");
            String out = gson.toJson(createLocReqJSON(address),GeoCodingLocRequest.class);
            byte[] outAsBytes = out.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(outAsBytes);
            BufferedReader output = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
            String outputAsString = output.lines().collect(Collectors.joining());
            locResponse = gson.fromJson(outputAsString, GeocodingLocResponse.class);
            http.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return locResponse;
    }

    public int calculateDistance(String address1, String address2) throws RuntimeException {
        int distance;
        GeocodingDistanceResponse distanceResponse;
        Gson gson = new Gson();

        try {
            URL url = new URL("http://10.10.10.83:8080/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");
            String out = gson.toJson(createDistReqJSON(address1, address2));
            byte[] outAsByte = out.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(outAsByte);
            BufferedReader output = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
            String outputAsString = output.lines().collect(Collectors.joining());
            distanceResponse = gson.fromJson(outputAsString, GeocodingDistanceResponse.class);
            http.disconnect();
            distance = distanceResponse.getDistanceKm().intValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return distance;
    }

    public GeoCodingDistanceRequest createDistReqJSON(String address1, String address2) {
        GeoCodingDistanceRequest distanceRequest = new GeoCodingDistanceRequest();
        distanceRequest.addPoint(getResponse(address1).getEntries().stream().mapToDouble(GeocodingLocRespEntry::getLat).sum(), getResponse(address1).getEntries().stream().mapToDouble(GeocodingLocRespEntry::getLon).sum());
        distanceRequest.addPoint(getResponse(address2).getEntries().stream().mapToDouble(GeocodingLocRespEntry::getLat).sum(), getResponse(address2).getEntries().stream().mapToDouble(GeocodingLocRespEntry::getLon).sum());
        return distanceRequest;
    }

    public GeoCodingLocRequest createLocReqJSON(String address) {
        GeoCodingLocRequest locRequest = new GeoCodingLocRequest();
        locRequest.setAddress(address);
        locRequest.setLimit(1);
        return locRequest;
    }
}