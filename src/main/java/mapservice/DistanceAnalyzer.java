package mapservice;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import netscape.javascript.JSObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.defaultCharset;

public class DistanceAnalyzer {


    public JSONPObject getRespEntryJSON(String address) {
        assert false;
        JSONPObject respEntryJSON;
        try {
            URL url = new URL("http://10.10.10.83:8080/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");
            byte[] out = createLocReqJSON(address, "1").getBytes(StandardCharsets.UTF_8);
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            BufferedReader output = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
            String stringAsOutput = output.lines().collect(Collectors.joining());
            JSONParser parser = new JSONParser();
            respEntryJSON = (JSONObject) parser.parse(stringAsOutput);
            http.disconnect();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return respEntryJSON;
    }

    public Double calculateDistance(String address1, String address2) throws RuntimeException {
        double distance;
        try {
            URL url = new URL("http://10.10.10.83:8080/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");
            byte[] out = createDistReqJSON(address1, address2).getBytes(defaultCharset());
            OutputStream stream = http.getOutputStream();
            stream.write(out);
            BufferedReader output = new BufferedReader(new InputStreamReader(http.getInputStream()));
            JsonParser parser = new JsonParser();
            JSObject disRespJSON = new JSObject(output) {
            };
            http.disconnect();
            distance = (Double) disRespJSON.get("distanceKm");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return distance;
    }

    public String createDistReqJSON(String address1, String address2) {
        ObjectMapper mapper = new ObjectMapper();

        double lat2 = Double.parseDouble(String.valueOf(getRespEntryJSON(address1).get("lat")));
        double lon1 = Double.parseDouble(String.valueOf(getRespEntryJSON(address1).get("lon")));
        double lon2 = Double.parseDouble(String.valueOf(getRespEntryJSON(address1).get("lon")));
        ObjectNode req = mapper.createObjectNode();
        req.put("lats", "[" + lat1 + ", " + lat2 + "]");
        req.put("lons", "[" + lon1 + ", " + lon2 + "]");
        String json;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public String createLocReqJSON(String address, String limit) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode req = mapper.createObjectNode();
        req.put("address", address);
        req.put("limit", limit);
        String json;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}