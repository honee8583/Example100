package com.example.example100.pharmacy.controller;

import com.example.example100.pharmacy.model.PharmacyResult;
import com.example.example100.pharmacy.model.PharmacySearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collections;
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
public class PharmacyApiController {
    @Value("${openApi.pharmacy.serviceKey}")
    private String SERVICE_KEY;

    /**
     * 86. RestTemplate을 이용한 공공데이터포털의 api를 연동하여 전국약국목록을 가져오는 api를 작성하시오.
     * 결과값을 단순 문자열로 반환.
     */
    @GetMapping("/api/pharmacy")
    public String pharmacy() {
        String url = "https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown?serviceKey=%s&pageNo=1&numOfRows=10&ORD=NAME";
        String apiResult = "";
        try {
            URI uri = new URI(String.format(url, SERVICE_KEY));
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            apiResult = restTemplate.getForObject(uri, String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return apiResult;
    }

    /**
     * 87. 전국약국목록 검색 결과를 모델로 매핑하여 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/pharmacy2")
    public ResponseEntity<PharmacyResult> pharmacy2() {
        String url = "https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire?serviceKey=%s&pageNo=1&numOfRows=10";
        String apiResult = "";
        try {
            URI uri = new URI(String.format(url, SERVICE_KEY));
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            apiResult = restTemplate.getForObject(uri, String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        PharmacyResult jsonResult = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonResult = objectMapper.readValue(apiResult, PharmacyResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(jsonResult);
    }

    /**
     * 88. 전국약국목록 검색 결과를 모델로 매핑하여 리턴하는 api를 작성하시오.
     * 시도/구군 단위로 검색 기능 추가.
     */
    @GetMapping("/api/pharmacy3")
    public ResponseEntity<?> pharmacy3(@RequestBody PharmacySearch search) {
        String url = String.format("https://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire?serviceKey=%s&pageNo=1&numOfRows=10", SERVICE_KEY);
        String apiResult = "";
        try {
            url += String.format("&Q0=%s&Q1=%s"
                    , URLEncoder.encode(search.getSido(), "UTF-8")
                    , URLEncoder.encode(search.getGugun(), "UTF-8"));
            URI uri = new URI(url);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            apiResult = restTemplate.getForObject(uri, String.class);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        PharmacyResult jsonResult = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonResult = objectMapper.readValue(apiResult, PharmacyResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(jsonResult);
    }
}
