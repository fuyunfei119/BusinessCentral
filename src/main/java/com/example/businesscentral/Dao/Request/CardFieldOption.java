package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardFieldOption {

    @JsonProperty("cardID")
    private String cardID;
    @JsonProperty("table")
    private String table;
    @JsonProperty("fieldName")
    private String fieldName;
}
