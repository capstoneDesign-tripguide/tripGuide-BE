package com.tripGuide.server.trip.api;

import com.tripGuide.server.trip.api.request.PlanDto;
import com.tripGuide.server.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class TripController {

    private final TripService tripService;

    @PostMapping("/plan")
    public Map<String, String> makePlan(@RequestBody PlanDto planDto) throws JSONException, IOException {
        String makedPlan = tripService.makePlan(planDto);
        Map<String, String> response = new HashMap<>();
        response.put("example", makedPlan);
        return response;
    }
}
