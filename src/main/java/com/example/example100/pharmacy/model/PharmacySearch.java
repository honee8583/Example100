package com.example.example100.pharmacy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacySearch {
    private String sido;
    private String gugun;

    public String getSido() {
        return sido != null ? sido : "";
    }

    public String getGugun() {
        return gugun != null ? gugun : "";
    }
}
