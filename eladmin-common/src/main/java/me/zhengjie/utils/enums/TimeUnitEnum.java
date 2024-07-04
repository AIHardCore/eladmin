package me.zhengjie.utils.enums;

public enum TimeUnitEnum {
    MONTH(0, "月"),
    YEAR(1, "年");

    private Integer value;
    private String name;

    TimeUnitEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }
    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static TimeUnitEnum convert(String name){
        return TimeUnitEnum.valueOf(name);
    }

    public static TimeUnitEnum convert(int value){
        TimeUnitEnum[] enums = TimeUnitEnum.values();
        for(TimeUnitEnum e : enums){
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
