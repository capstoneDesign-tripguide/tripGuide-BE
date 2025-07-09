package com.tripGuide.server.trip.utils;

import com.tripGuide.server.trip.domain.Place;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@NoArgsConstructor
public class DistanceMatrixCalculator {
    @Value("${GOOGLE_API_KEY}")
    private String googleApiKey;
    public int[][] makedistanceMatrix(int day, List<Place> sortedPlaces) {
        int size = day * 3;
        int[][] matrix = new int[size][size];

        if (sortedPlaces.size() < size) {
            throw new IllegalArgumentException("Not enough places to create a distance matrix for the given number of days.");
        }

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (i == j) {
                    matrix[i][j] = 999999999;
                    continue;
                }
                String ori = sortedPlaces.get(i).getLocation().getLatitude() + "," + sortedPlaces.get(i).getLocation().getLongitude();
                String dst = sortedPlaces.get(j).getLocation().getLatitude() + "," + sortedPlaces.get(j).getLocation().getLongitude();
                String mode = "transit";
                try {
                    long temp = getSpendTime(ori, dst, mode);
                    matrix[i][j] = (int) temp;
                    matrix[j][i] = (int) temp;
                } catch (Exception e) {
                    matrix[i][j] = 999999999;
                    matrix[j][i] = 999999999;
                }
            }
        }
        return matrix;
    }

    public int getSpendTime(String origin, String destination, String mode) throws IOException {
        String urlStr = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin
                + "&destination=" + destination
                + "&mode=" + mode
                + "&departure_time=now"
                + "&language=ko"
                + "&key=" + googleApiKey;

        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // 응답 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // JSON 파싱
            JSONObject responseJson = new JSONObject(response.toString());
            if (responseJson.getJSONArray("routes").length() > 0) {
                return responseJson.getJSONArray("routes")
                        .getJSONObject(0)
                        .getJSONArray("legs")
                        .getJSONObject(0)
                        .getJSONObject("duration")
                        .getInt("value");
            } else {
                return 99999; // 기본값 반환
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return 99999; // 예외 처리 시 기본값 반환
        }
    }
}
