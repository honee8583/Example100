package com.example.example100.pharmacy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyResultBodyItem {
    private String dutyAddr;
    private String dutyMapimg;
    private String dutyName;
    private String dutyTel1;
    private int dutyTime1c;
    private String dutyTime1s;
    private int dutyTime2c;
    private String dutyTime2s;
    private int dutyTime3c;
    private String dutyTime3s;
    private int dutyTime4c;
    private String dutyTime4s;
    private int dutyTime5c;
    private String dutyTime5s;
    private int dutyTime6c;
    private String dutyTime6s;
    private String hpid;
    private String postCdn1;
    private String postCdn2;
    private int rnum;
    private double wgs84Lat;
    private double wgs84Lon;
}
