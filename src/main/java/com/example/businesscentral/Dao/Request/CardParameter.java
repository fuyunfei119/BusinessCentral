package com.example.businesscentral.Dao.Request;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class CardParameter {
    private String table;
    private String page;
    private String recordID;
}
