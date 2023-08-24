package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardParameterUpToDate {
    @JsonProperty("field")
    private String field;
    @JsonProperty("value")
    private Object value;
}
