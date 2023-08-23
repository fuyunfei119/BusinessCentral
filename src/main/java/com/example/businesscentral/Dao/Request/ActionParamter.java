package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActionParamter {
    @JsonProperty("page")
    private String page;
    @JsonProperty("actionName")
    private String actionName;
}
