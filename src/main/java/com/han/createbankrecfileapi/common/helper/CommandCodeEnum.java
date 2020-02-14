package com.han.createbankrecfileapi.common.helper;

public enum CommandCodeEnum implements LabelAndValue<String>{



    RealTimeSingleTrans("10009","单笔实时代收付"),//实时单笔代收付交易

    SingleTest("01031","测试");



    private String value;
    private String label;
    CommandCodeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
    public static CommandCodeEnum getByValue(String value){
        for(CommandCodeEnum commandCodeEnum : values()){
            if (value.equals(commandCodeEnum.getValue())) {
                return commandCodeEnum;
            }
        }
        return null;
    }
}