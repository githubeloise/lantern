package com.lamp.lantern.plugins.core.login.times;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lamp.decoration.core.result.ResultObject;
import com.lamp.lantern.plugins.api.mode.UserInfo;
import com.lamp.lantern.plugins.core.login.AbstrackAuthHandler;
import com.lamp.lantern.plugins.core.login.LanternContext;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginTimesAuthHandler extends AbstrackAuthHandler<LoginTimesConfig> {

	private static final String VALUE_KEY = "loginTimesAuthHandler";

	private String addressTimeKey;

	private String userIdentificationKey;

	private ResultObject<String> error;

	@Override
	public void init() {
		error = ResultObject.getResultObjectMessgae(3000, "登录失败次数到限制，请明天登录");
		this.addressTimeKey = this.systemName + "-" + "address-time";
		this.userIdentificationKey = this.systemName + "-" + "user-time";
	}

	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	@Override
	public ResultObject<String> authBefore(UserInfo userInfo) {
		LoginTimesInfo loginTimesInfo = new LoginTimesInfo();
		LanternContext.getContext().setValue(VALUE_KEY, loginTimesInfo);
		if (config.getAddressTimeLong() > 0) {
			HttpServletRequest request = getRequest();
			String IP = request.getHeader("x-forwarded-for");
			String key = this.addressTimeKey + (Objects.isNull(IP) ? request.getRemoteAddr() : IP);
			loginTimesInfo.setAddressLimesKey(key);
			Integer integer = Integer.valueOf(connection.sync().get(key));
			loginTimesInfo.setAddressLimes(Objects.isNull(integer) ? -1 : integer);
		}
		if (config.getTimes() > 0) {
			String key =this.userIdentificationKey +( Objects.nonNull(userInfo.getUiName()) ? userInfo.getUiName()
					: Objects.nonNull(userInfo.getUiEmail()) ? userInfo.getUiEmail() : userInfo.getUiPhone());
			loginTimesInfo.setTimesKey(key);
			Integer integer = Integer.valueOf(connection.sync().get(key));
			loginTimesInfo.setTimes(Objects.isNull(integer) ? -1 : integer);
		}
		if (loginTimesInfo.getTimes() >= config.getTimes()
				|| loginTimesInfo.getAddressLimes() >= config.getAddressLimes()) {
			log.warn("");
			return error;
		}
		return null;

	}

	@Override
	public void doAuthAfter(UserInfo userInfo) {
		LoginTimesInfo loginTimesInfo = LanternContext.getContext().getValue(VALUE_KEY);
		if (Objects.nonNull(loginTimesInfo)) {
			return;
		}
		if (loginTimesInfo.getTimes() != -1) {
			connection.async().del(loginTimesInfo.getTimesKey());
		}
		if (loginTimesInfo.getAddressLimes() != -1) {
			connection.async().del(loginTimesInfo.getAddressLimesKey());
		}
	}

	@Override
	public void doError(UserInfo userInfo) {
		LoginTimesInfo loginTimesInfo = LanternContext.getContext().getValue(VALUE_KEY);
		// 当times大于0，表示启动用户限制
		if (config.getTimes() > 0) {
			if (loginTimesInfo.getTimes() < config.getTimes()) {
				connection.async().incr(loginTimesInfo.getTimesKey());
				if (Objects.equals(TimeUnit.DAYS, config.getAddressTimeUnit()) && loginTimesInfo.getTimes()==-1) {
					connection.async().expireat(loginTimesInfo.getTimesKey(), Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
				}else {
					connection.async().expire(loginTimesInfo.getTimesKey(), config.getTimes());
				}
			}
		}
		// 当addresstimes大于0，表示启动IP限制
		if (config.getAddressLimes() > 0) {
			// 当前限制数 小于 规定限制数量就小于自增，并且规定时间
			if (loginTimesInfo.getAddressLimes() < config.getAddressLimes()) {
				// 自增
				connection.async().incr(loginTimesInfo.getAddressLimesKey());
				//单位是天，且是第一次
				if (Objects.equals(TimeUnit.DAYS, config.getAddressTimeUnit()) && loginTimesInfo.getAddressLimes()==-1) {
					connection.async().expireat(loginTimesInfo.getAddressLimesKey(), Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
				} else {
					connection.async().expire(loginTimesInfo.getAddressLimesKey(), config.getAddressLimes());
				}
			}
		}
	}

	@Data
	static class LoginTimesInfo {

		private Integer times;

		private Integer addressLimes;

		private String timesKey;

		private String addressLimesKey;

	}
}
