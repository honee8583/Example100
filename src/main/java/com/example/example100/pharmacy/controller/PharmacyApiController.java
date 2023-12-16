package com.example.example100.pharmacy.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

}
