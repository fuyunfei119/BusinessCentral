package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PageParameter {
    @JsonProperty("pageName")
    private String pageName;
}
