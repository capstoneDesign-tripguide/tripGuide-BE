package com.tripGuide.server.trip.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Location {
    public Double latitude;
    public Double longitude;

    public String toString() {
        return "locate{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
    public JSONObject toJson() throws JSONException {
        JSONObject locJson = new JSONObject();
        locJson.put("latitude", latitude);
        locJson.put("longitude", longitude);
        return locJson;
    }
}
