package com.example.businesscentral.Dao.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@Accessors(chain = true)
public class DeleteRecord {
    @JsonProperty("table")
    private String table;
    @JsonProperty("records")
    private List<LinkedHashMap<String,Object>> records;
}
