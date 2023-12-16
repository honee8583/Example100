package com.example.example100.airpollution.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirPollutionSearch {
    private String sidoName;

    public String getSidoName() {
        return sidoName != null ? sidoName : "";
    }
}
