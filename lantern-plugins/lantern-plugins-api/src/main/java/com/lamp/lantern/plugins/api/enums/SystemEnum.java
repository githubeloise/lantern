package com.lamp.lantern.plugins.api.enums;

import lombok.Getter;

public enum SystemEnum {
    MACOS("MACOS"),
    WINDOWS("WINDOWS"),
    LINUX("LINUX"),
    IOS("IOS"),
    ANDROID("ANDROID"),
    HARMONY("HARMONY"),
    OTHER("OTHER");

	@Getter
    private String system;

    private SystemEnum(String system){
        this.system = system;
    }
}
