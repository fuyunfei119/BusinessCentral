package com.example.businesscentral.Dao.Response;

import lombok.Data;

import java.util.List;

@Data
public class CardOptionResponse {
    private String fieldType;
    private List<?> fieldOptions;
}
