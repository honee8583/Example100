package com.example.example100.airpollution.controller;

import com.example.example100.airpollution.model.AirPollutionResponse;
import com.example.example100.airpollution.model.AirPollutionSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collections;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AirPollutionController {
    @Value("${openApi.airPollution.serviceKey}")
    private String SERVICE_KEY;

    private final String ENCODE_TYPE = "UTF-8";

    /**
     * 89. 미세먼지 정보 조회 api를 사용하여 결과를 반환하는 api 작성하시오.
     * xml -> Java Object XmlMapper 사용하기
     */
    @GetMapping("/api/airpollution")
    public ResponseEntity<?> airPollution2(@RequestBody AirPollutionSearch search) {
        AirPollutionResponse response = null;
        try {
            String url = String.format("https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=%s&numOfRows=10&pageNo=1&sidoName=%s&ver=1.0"
                    , SERVICE_KEY
                    , URLEncoder.encode(search.getSidoName(), ENCODE_TYPE));
            URI uri = new URI(url);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders header = new HttpHeaders();
            header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            String result = restTemplate.getForObject(uri, String.class);

            log.info(result);

            XmlMapper xmlMapper = new XmlMapper();
            response = xmlMapper.readValue(result, AirPollutionResponse.class);
        } catch (URISyntaxException | UnsupportedEncodingException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 89. 미세먼지 정보 조회 api를 사용하여 결과를 반환하는 api 작성하시오.
     * JAXB와 Unmarshaller 사용해서 파싱하기
     * 아래의 의존성 주입 필요
     * javax.xml.bind:jaxb-api:2.3.1
     * com.sun.xml.bind:jaxb-impl
     * org.glassfish.jaxb:jaxb-core
     */
    @GetMapping("/api/airpollution2")
    public ResponseEntity<?> airPollution3(@RequestBody AirPollutionSearch search) {
        AirPollutionResponse apiResponse = null;
        try {
            String url = String.format("https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=%s&numOfRows=10&pageNo=1&sidoName=%s&ver=1.0"
                    , SERVICE_KEY
                    , URLEncoder.encode(search.getSidoName(), ENCODE_TYPE));
            URI uri = new URI(url);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders header = new HttpHeaders();
            header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            String xml = restTemplate.getForObject(uri, String.class);

            log.info(xml);

            JAXBContext jaxbContext = JAXBContext.newInstance(AirPollutionResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            apiResponse = (AirPollutionResponse) unmarshaller.unmarshal(new StringReader(xml));
        } catch (URISyntaxException | UnsupportedEncodingException | JAXBException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(apiResponse);
    }
}
