package com.example.example100.naver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// 대규모 프로젝트에서는 @ConfigurationProperties를 이용하여 프로퍼티를 그룹화하고 타입 안정성을 확보한다.
@Data
@Component
@ConfigurationProperties("naver-api")
public class NaverAppProperties {
    private String clientId;
    private String clientSecret;
}
