package com.example.example100.pharmacy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyResultResponse {
    private PharmacyResultHeader header;
    private PharmacyResultBody body;
}
