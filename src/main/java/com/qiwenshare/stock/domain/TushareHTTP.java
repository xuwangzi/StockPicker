package com.qiwenshare.stock.domain;

import lombok.Data;

@Data
public class TushareHTTP {
    /**
     * Api
     */
    private String api_name;
    /**
     * Token
     */
    private String token;
    /**
     * params
     */
    private Object params;
    /**
     * fields
     */
    private String fields;
}
