package com.example.example100.naver.controller;

import com.example.example100.naver.config.NaverAppProperties;
import com.example.example100.naver.model.NaverTranslateInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiNaverController {

//    @Value("${openApi.naver.client_id}")
//    private String CLIENT_ID;
//
//    @Value("${openApi.naver.client_secret}")
//    private String CLIENT_SECRET;

    private final NaverAppProperties naverAppProperties;

    private final String CLIENT_ID_HEADER = "X-Naver-Client-Id";
    private final String CLIENT_SECRET_HEADER = "X-Naver-Client-Secret";
    private final String URL = "https://openapi.naver.com/v1/papago/n2mt";

    /**
     * 91. naver open api를 이용하여 번역 api 작성하시오.
     * 94. applicationl.yml파일에 작성한 프로퍼티 내용들을 프로퍼티 클래스로 분리하시오.
     */
    @GetMapping("/api/translate")
    public ResponseEntity<?> translate(@RequestBody NaverTranslateInput input) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("source", "ko");
        parameters.add("target", "en");
        parameters.add("text", input.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(CLIENT_ID_HEADER, naverAppProperties.getClientId());
        headers.add(CLIENT_SECRET_HEADER, naverAppProperties.getClientSecret());

        HttpEntity formEntity = new HttpEntity<>(parameters, headers);
        return restTemplate.postForEntity(URL, formEntity, String.class);
    }
}
