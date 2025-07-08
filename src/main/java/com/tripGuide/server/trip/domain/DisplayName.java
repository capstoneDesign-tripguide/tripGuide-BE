package com.tripGuide.server.trip.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisplayName {
    public String text = "";
    public String languageCode = "";

    public JSONObject toJson() throws JSONException {
        JSONObject nameJson = new JSONObject();
        nameJson.put("text", text);
        nameJson.put("languageCode", languageCode);
        return nameJson;
    }
}
