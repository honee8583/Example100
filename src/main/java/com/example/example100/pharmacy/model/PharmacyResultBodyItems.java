package com.example.example100.pharmacy.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyResultBodyItems {
    private List<PharmacyResultBodyItem> item;
}
