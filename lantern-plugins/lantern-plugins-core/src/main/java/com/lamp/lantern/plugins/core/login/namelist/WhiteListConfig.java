/*
 *Copyright (c) [Year] [name of copyright holder]
 *[Software Name] is licensed under Mulan PubL v2.
 *You can use this software according to the terms and conditions of the Mulan PubL v2.
 *You may obtain a copy of Mulan PubL v2 at:
 *         http://license.coscl.org.cn/MulanPubL-2.0
 *THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *See the Mulan PubL v2 for more details.
 */
package com.lamp.lantern.plugins.core.login.namelist;

import lombok.Data;

@Data
public class WhiteListConfig {

	/**
	 * 传入:
	 * "entity" 直接识别实体类的状态
	 *
	 */
	private String whiteListSourceType;

	/**
	 * 是白名单，还是黑名单
	 */
	private WhiteListType whiteListType;

	enum WhiteListType{
		WHITE("白名单"),
		BLACK("黑名单");

		WhiteListType(String description){}
	}

}
