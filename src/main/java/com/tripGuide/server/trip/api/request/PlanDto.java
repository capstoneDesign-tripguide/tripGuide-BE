package com.tripGuide.server.trip.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PlanDto {

    private List<String> tags;

    private int day;

    @NotBlank
    private String place;
}
