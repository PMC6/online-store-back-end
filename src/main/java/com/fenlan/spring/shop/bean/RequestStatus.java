package com.fenlan.spring.shop.bean;

public enum  RequestStatus {
    PROCESS(0, "申请中"),
    APPROVE(1, "审核通过"),
    REJECT(2, "审核不通过");

    RequestStatus(int number, String description) {
        this.code = number;
        this.description = description;
    }

    private final int code;
    private String description;

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RequestStatus getByCode(int code) {
        switch (code) {
            case 0 : return PROCESS;
            case 1 : return APPROVE;
            case 2 : return REJECT;
            default : return null;
        }
    }
}
