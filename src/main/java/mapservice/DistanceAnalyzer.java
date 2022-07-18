package mapservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import pl.adampolsa.mapservice.msg.request.GeoCodingDistanceRequest;
import pl.adampolsa.mapservice.msg.request.GeoCodingLocRequest;
import pl.adampolsa.mapservice.msg.response.GeocodingDistanceResponse;
import pl.adampolsa.mapservice.msg.response.GeocodingLocRespEntry;
import pl.adampolsa.mapservice.msg.response.GeocodingLocResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class DistanceAnalyzer {


    public GeocodingLocResponse getResponse(String address) {
        ObjectMapper mapper = new ObjectMapper();
        GeocodingLocResponse locResponse;
        try {
            URL url = new URL("http://10.10.10.83:8080/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");
            byte[] out = mapper.writeValueAsBytes(createLocReqJSON(address));
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            BufferedReader output = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
            locResponse = mapper.readValue(output, GeocodingLocResponse.class);
            http.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return locResponse;
    }

    public int calculateDistance(String address1, String address2) throws RuntimeException {
        int distance;
        GeocodingDistanceResponse distanceResponse;
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL url = new URL("http://10.10.10.83:8080/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");
            byte[] out = mapper.writeValueAsBytes(createDistReqJSON(address1, address2));
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            BufferedReader output = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
            distanceResponse = mapper.readValue(output, GeocodingDistanceResponse.class);
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