package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardPageID {
    @JsonProperty("cardID")
    private String cardID;
    @JsonProperty("table")
    private String table;
    @JsonProperty("recordID")
    private String recordID;
}
