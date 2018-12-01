package com.fenlan.spring.shop.bean;

import java.util.Date;

public class ResponseFormat {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Object data;

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Object getData() {
        return data;
    }

    public static class Builder {
        private Date timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private Object data;

        public Builder(Date timestamp, int status) {
            this.timestamp = timestamp;
            this.status = status;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseFormat build() {
            return new ResponseFormat(this);
        }
    }

    public ResponseFormat(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.path = builder.path;
        this.data = builder.data;
    }
}
