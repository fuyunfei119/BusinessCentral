package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Data
@Accessors(chain = true)
public class CardGroup {
    @JsonProperty("groupName")
    private String groupName;
    @JsonProperty("fields")
    private LinkedHashMap<String,CardField> fields;
}
