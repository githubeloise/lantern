package com.lamp.lantern.plugins.api.auth.config;

import com.lamp.lantern.plugins.api.auth.AuthenticationDataService;
import lombok.Data;

@Data
public class LocalCacheConfig {

    private DubboAuthenticationConfig dubboAuthenticationConfig;

    private RedisCacheConfig redisCacheConfig;

    private AuthenticationDataService authOperation;

    private String authenticationDataServiceName;

    private Long dataSyncInterval;
}
