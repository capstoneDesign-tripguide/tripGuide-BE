package com.tripGuide.server.trip.service;

import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.tripGuide.server.trip.api.request.PlanDto;
import com.tripGuide.server.trip.domain.DisplayName;
import com.tripGuide.server.trip.domain.Location;
import com.tripGuide.server.trip.domain.Place;
import com.tripGuide.server.trip.utils.DistanceMatrixCalculator;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TripService {
    @Value("${GOOGLE_API_KEY}")
    private String GOOGLE_API_KEY;

    public String makePlan(PlanDto planDto)throws JSONException, IOException {
        String result = autoMode(planDto.getPlace(), planDto.getTags(), planDto.getDay());
        return result;
    }

    public String autoMode(String place, List<String> tagList, int day) throws IOException, JSONException {
        List<Place> places = findPlace(place, tagList);
        if (places.size() < day * 3) { // 장소들은 하루에 최소 3개씩 잡아놓음.
            throw new IllegalArgumentException("Not enough places found for the given number of days and tags.");
        }
        DistanceMatrixCalculator distanceMatrixCalculator = new DistanceMatrixCalculator();
        int[][] distanceMatrix = distanceMatrixCalculator.makedistanceMatrix(day, places);
        int[][] mstMatrix = primAlgorithmFinal(distanceMatrix);
        List<Integer> route = findRoute(mstMatrix);
        System.out.println(route.size());
        boolean err = false;
        int resultDist = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            if (distanceMatrix[route.get(i)][route.get(i + 1)] == 999999999) {
                err = true;
                break;
            }
            resultDist += distanceMatrix[route.get(i)][route.get(i + 1)];
        }

        if (!err) {
            System.out.println(resultDist);
        } else {
            System.out.println("Error occurred");
            return "";
        }


        JSONArray jsonPlacesPerDay = new JSONArray();
        for (int i = 0; i < day; i++) {
            JSONArray dayArray = new JSONArray();
            for (int j = 0; j < 3; j++) {
                int idx = i * 3 + j;
                JSONObject jsonPlace = places.get(idx).toJson();  // 관광지를 JSON 객체로 추가

                if (j == 0) { // 아침
                    JSONObject rest = findRestaurant(places.get(idx), places.get(idx));
                    dayArray.put(rest);
                    dayArray.put(jsonPlace);
                } else if (j == 2) { // 저녁
                    JSONObject rest = findRestaurant(places.get(idx), places.get(idx));
                    dayArray.put(jsonPlace);
                    dayArray.put(rest);
                } else { // 점심
                    JSONObject rest = findRestaurant(places.get(idx - 1), places.get(idx));
                    dayArray.put(rest);
                    dayArray.put(jsonPlace);
                }
            }
            jsonPlacesPerDay.put(dayArray);
        }

        return jsonPlacesPerDay.toString(4);
    }

    public List<Place> findPlace(String place, List<String> tagList) throws JSONException {
        Set<Place> placeSet = new HashSet<>();
        String query = String.join(", ", tagList) + " tourist attraction in " + place;
        System.out.println(query);
        String translatedQuery = null;
        try {
            // 쿼리를 영어로 번역
            translatedQuery = translateText(query);
        } catch (com.deepl.api.DeepLException | InterruptedException e) {
            e.printStackTrace();
            return new ArrayList<>(); // 번역 실패 시 빈 리스트 반환
        }
        System.out.println(translatedQuery);

        JSONObject data = new JSONObject();
        data.put("textQuery", translatedQuery);
        data.put("languageCode", "ko");
        String response = null; //여기까지 쿼리 받았고 data에는 영어로 번역된 쿼리, ko
        try {
            response = performHttpPostRequest("https://places.googleapis.com/v1/places:searchText", data.toString(), GOOGLE_API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        // 결과를 출력하여 디버깅
        System.out.println("API Response: " + response);

        JSONObject jsonResponse;
        JSONArray placesArray;
        try {
            jsonResponse = new JSONObject(response);
            placesArray = jsonResponse.getJSONArray("places"); // API 응답의 구조에 따라 변경
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        for (int i = 0; i < placesArray.length(); i++) {
            JSONObject placeObj = placesArray.getJSONObject(i);
            Place p = new Place();
            p.setId(placeObj.optString("id", "unknown_id")); // 기본값으로 "unknown_id" 설정
            p.setRating(placeObj.optDouble("rating", 0.0));

            try {
                JSONObject locObj = placeObj.getJSONObject("location");
                Location loc = new Location();
                loc.setLatitude(locObj.getDouble("latitude"));
                loc.setLongitude(locObj.getDouble("longitude"));
                p.setLocation(loc);
            } catch (JSONException e) {
                System.out.println("Location information not found in JSON object: " + placeObj);
                continue; // 키가 없을 경우 다음 장소로 넘어감
            }

            // name 필드가 없는 경우 기본값 설정
            String nameText = placeObj.optString("displayName", null);
            if (nameText == null || nameText.isEmpty()) {
                // 기본값 설정
                nameText = "Unknown Place";
            } else {
                // JSON 형식의 문자열이 아닌 순수 문자열을 사용하도록 처리
                try {
                    JSONObject displayNameJson = new JSONObject(nameText);
                    nameText = displayNameJson.optString("text", "Unknown Place");
                } catch (JSONException e) {
                    System.out.println("Invalid JSON format for 'displayName': " + nameText);
                    nameText = "Unknown Place";
                }
            }
            p.setDisplayName(new DisplayName(nameText, "ko")); // languageCode를 "ko"로 설정

            placeSet.add(p);
        }

        List<Place> places = new ArrayList<>(placeSet);
        places.sort((p1, p2) -> Double.compare(p2.getRating(), p1.getRating())); // 내림차순 정렬

        if (places.isEmpty()) {
            System.out.println("No places found with the given tags and location.");
        }

        return places;
    }

    private String translateText(String text) throws com.deepl.api.DeepLException, InterruptedException {
        String authKey = "e0e9745a-f486-47fc-b5d7-3d9280d628ba:fx";
        Translator translator = new Translator(authKey);
        TextResult result = null;
        try {
            result = translator.translateText(text, null, "en-US");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }
        return result != null ? result.getText() : "";
    }

    private String performHttpPostRequest(String urlString, String jsonData, String apiKey) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-Goog-Api-Key", apiKey);
        connection.setRequestProperty("X-Goog-FieldMask", "places.id,places.rating,places.location,places.displayName");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public int[][] primAlgorithmFinal(int[][] graph) {
        int n = graph.length;
        boolean[] visited = new boolean[n];
        PriorityQueue<int[]> edgeHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        edgeHeap.add(new int[]{0, 0, -1}); // (가중치, 현재 정점, 이전 정점)

        List<int[]> mstEdges = new ArrayList<>();

        while (mstEdges.size() < n - 1) {
            int[] edge = edgeHeap.poll();
            int weight = edge[0];
            int current = edge[1];
            int prev = edge[2];

            if (visited[current]) {
                continue;
            }

            visited[current] = true;
            if (prev != -1) {
                mstEdges.add(new int[]{prev, current, weight});
            }

            for (int nextVertex = 0; nextVertex < n; nextVertex++) {
                int edgeWeight = graph[current][nextVertex];
                if (!visited[nextVertex] && edgeWeight != 0) {
                    edgeHeap.add(new int[]{edgeWeight, nextVertex, current});
                }
            }
        }

        int[][] mstMatrix = new int[n][n];
        for (int[] e : mstEdges) {
            int i = e[0], j = e[1], k = e[2];
            mstMatrix[i][j] = k;
            mstMatrix[j][i] = k;
        }
        return mstMatrix;
    }

    public List<Integer> findRoute(int[][] mstMatrix) {
        List<Integer> route = new ArrayList<>();
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(0);

        while (!queue.isEmpty()) {
            int node = queue.pop();
            route.add(node);

            for (int i = 0; i < mstMatrix[node].length; i++) {
                if (!route.contains(i) && mstMatrix[node][i] > 0) {
                    queue.add(i);
                }
            }
        }
        return route;
    }

    public JSONObject findRestaurant(Place place1, Place place2) throws IOException, JSONException {
        double lat, lon;
        Set<String> visitedRestaurants = new HashSet<>();
        if (place2 == null) {
            lat = place1.getLocation().getLatitude();
            lon = place1.getLocation().getLongitude();
        } else {
            lat = (place1.getLocation().getLatitude() + place2.getLocation().getLatitude()) / 2;
            lon = (place1.getLocation().getLongitude() + place2.getLocation().getLongitude()) / 2;
        }

        String urlStr = "https://places.googleapis.com/v1/places:searchNearby";
        int radius = 500;
        JSONObject responseJson = null;
        JSONArray places = new JSONArray();

        while (radius <= 3000 && (places.length() == 0 || responseJson == null)) {
            JSONObject payload = new JSONObject();
            payload.put("includedTypes", new JSONArray(Arrays.asList("restaurant")));
            payload.put("maxResultCount", 10);
            JSONObject locationRestriction = new JSONObject();
            locationRestriction.put("circle", new JSONObject()
                    .put("center", new JSONObject()
                            .put("latitude", lat)
                            .put("longitude", lon))
                    .put("radius", radius));
            payload.put("locationRestriction", locationRestriction);
            payload.put("languageCode", "ko");

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("X-Goog-Api-Key", GOOGLE_API_KEY);
            conn.setRequestProperty("X-Goog-FieldMask", "places.id,places.rating,places.location,places.displayName");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                responseJson = new JSONObject(response.toString());
                try {
                    places = responseJson.getJSONArray("places");
                } catch (JSONException e) {
                    System.out.println("Key 'places' not found in JSON response: " + responseJson);
                    places = new JSONArray(); // 'places' 키가 없을 경우 빈 배열로 설정
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            radius += 500; // 반경을 500씩 증가시킴
        }

        // 최고 평점 레스토랑 찾기
        for (int i = 0; i < places.length(); i++) {
            JSONObject restaurant = places.getJSONObject(i);
            String restaurantId = restaurant.getString("id");
            if (!visitedRestaurants.contains(restaurantId)) {
                visitedRestaurants.add(restaurantId);
                return restaurant; // 방문하지 않은 최고 평점 레스토랑 반환
            }
        }

        return getFallbackRestaurant(lat, lon); // 레스토랑이 없을 경우 fallback 레스토랑 반환
    }

    private JSONObject getFallbackRestaurant(double lat, double lon) throws JSONException {
        JSONObject fallbackRestaurant = new JSONObject();
        fallbackRestaurant.put("id", "fallback_id");
        fallbackRestaurant.put("rating", 0);
        fallbackRestaurant.put("location", new JSONObject().put("latitude", lat).put("longitude", lon));
        fallbackRestaurant.put("displayName", new JSONObject().put("text", "Fallback Restaurant").put("languageCode", "ko"));
        return fallbackRestaurant;
    }
}
