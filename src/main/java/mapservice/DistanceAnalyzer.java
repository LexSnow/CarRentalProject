package mapservice;


import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import pl.adampolsa.mapservice.msg.request.GeoCodingDistanceRequest;
import pl.adampolsa.mapservice.msg.request.GeoCodingLocRequest;
import pl.adampolsa.mapservice.msg.response.GeocodingDistanceResponse;
import pl.adampolsa.mapservice.msg.response.GeocodingLocRespEntry;
import pl.adampolsa.mapservice.msg.response.GeocodingLocResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;



public class DistanceAnalyzer {


    public GeocodingLocResponse getResponse(String address) throws IOException {
        GeocodingLocResponse locResponse;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://10.10.10.83:8080/");
        Gson gson = new Gson();
        String out = gson.toJson(createLocReqJSON(address), GeoCodingLocRequest.class);
        StringEntity stringEntity = new StringEntity(out, StandardCharsets.UTF_8);
        stringEntity.setContentEncoding(String.valueOf(StandardCharsets.UTF_8));
        stringEntity.setContentType(String.valueOf(StandardCharsets.UTF_8));
        post.setEntity(stringEntity);
        HttpResponse response = client.execute(post);
        String responseAsString = EntityUtils.toString(response.getEntity());
        locResponse = gson.fromJson(responseAsString, GeocodingLocResponse.class);
        return locResponse;
    }

    public int calculateDistance(String address1, String address2) throws RuntimeException, IOException {
        int distance;
        GeocodingDistanceResponse distanceResponse;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://10.10.10.83:8080/");
        Gson gson = new Gson();
        String out = gson.toJson(createDistReqJSON(address1, address2));
        StringEntity stringEntity = new StringEntity(out, StandardCharsets.UTF_8);
        stringEntity.setContentEncoding(String.valueOf(StandardCharsets.UTF_8));
        stringEntity.setContentType(String.valueOf(StandardCharsets.UTF_8));
        post.setEntity(stringEntity);
        HttpResponse response = client.execute(post);
        String responseAsString = EntityUtils.toString(response.getEntity());
        distanceResponse = gson.fromJson(responseAsString, GeocodingDistanceResponse.class);
        distance = distanceResponse.getDistanceKm().intValue();
        return distance;
    }

    public GeoCodingDistanceRequest createDistReqJSON(String address1, String address2) throws IOException {
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