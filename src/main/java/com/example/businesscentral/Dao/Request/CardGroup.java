package com.example.businesscentral.Dao.Request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;

@Data
@Accessors(chain = true)
public class CardGroup {
    private String groupName;
    private LinkedHashMap<String,Object> fields;
}
