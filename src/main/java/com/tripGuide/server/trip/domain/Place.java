package com.tripGuide.server.trip.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Place {
    private String id = "";
    private Location location;
    private Double rating;
    private DisplayName displayName;

    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", location=" + location.toString() +
                ", rating=" + rating +
                ", displayName=" + displayName +
                '}';
    }

    public JSONObject toJson() throws JSONException {
        JSONObject placeJson = new JSONObject();
        placeJson.put("id", this.id);
        placeJson.put("location", this.location.toJson());
        placeJson.put("rating", this.rating);
        if (this.displayName != null) {
            placeJson.put("displayName", this.displayName.toJson());
        }
        return placeJson;
    }
}
