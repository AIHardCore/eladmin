package me.zhengjie.utils.enums;

public enum LogTypeEnum {
    SYSTEM(0, "管理后台"),
    APP(1, "APP");

    private Integer value;
    private String name;

    LogTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static LogTypeEnum convert(String name){
        return LogTypeEnum.valueOf(name);
    }

    public static LogTypeEnum convert(int value){
        LogTypeEnum[] enums = LogTypeEnum.values();
        for(LogTypeEnum e : enums){
            if(e.value == value) {
                return e;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return this.name();
    }
}
