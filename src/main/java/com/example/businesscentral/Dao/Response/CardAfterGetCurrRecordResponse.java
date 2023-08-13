package com.example.businesscentral.Dao.Response;

import com.example.businesscentral.Dao.Request.CardGroup;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
public class CardAfterGetCurrRecordResponse {
    private List<CardGroup> cardGroups;
    private LinkedHashMap<String,Object> record;
}
