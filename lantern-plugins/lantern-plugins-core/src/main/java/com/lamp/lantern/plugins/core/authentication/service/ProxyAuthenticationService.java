package com.lamp.lantern.plugins.core.authentication.service;

import com.lamp.lantern.plugins.api.auth.AuthenticationData;
import com.lamp.lantern.plugins.api.auth.AuthenticationService;
import com.lamp.lantern.plugins.api.auth.AuthenticationServiceResult;
import lombok.Setter;

/**
 * 1. 获得用户信息可以使用 缓存
 * 2. 使用本地缓存
 * 3. 直接存续db， 启动的时候传入
 * 4. dubbo服务， 启动的时候传入
 * 5. http 请求， 启动的时候传入
 * @author laohu
 */
public class ProxyAuthenticationService implements AuthenticationService {

    @Setter
    private AuthenticationService userInfoService;

    @Setter
    private AuthenticationService authenticationService;

    @Override
    public AuthenticationServiceResult getUserInfo(AuthenticationData authData) {
        return userInfoService.getUserInfo(authData);
    }

    @Override
    public AuthenticationServiceResult authentication(AuthenticationData authData) {
        return userInfoService.authentication(authData);
    }
}
