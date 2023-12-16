package com.example.example100.airpollution.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="response")
public class AirPollutionResponse {
    @XmlElement(name = "header")
    private AirPollutionHeader header;
    @XmlElement(name = "body")
    private AirPollutionBody body;

    @Getter
    @Setter
    @XmlRootElement(name = "header")
    public static class AirPollutionHeader {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    @XmlRootElement(name = "body")
    public static class AirPollutionBody {
        private AirPollutionBodyItems items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;

        @Getter
        @Setter
        @XmlRootElement(name = "items")
        public static class AirPollutionBodyItems {
            private List<AirPollutionBodyItem> item;

            @Getter
            @Setter
            @XmlRootElement(name = "item")
            public static class AirPollutionBodyItem {
                private String so2Grade;
                private String coFlag;
                private String khaiValue;
                private String so2Value;
                private String coValue;
                private String pm25Flag;
                private String pm10Flag;
                private String o3Grade;
                private String pm10Value;
                private String khaiGrade;
                private String pm25Value;
                private String sidoName;
                private String no2Flag;
                private String no2Grade;
                private String o3Flag;
                private String pm25Grade;
                private String so2Flag;
                private String dataTime;
                private String coGrade;
                private String no2Value;
                private String stationName;
                private String pm10Grade;
                private String o3Value;
            }
        }
    }
}

