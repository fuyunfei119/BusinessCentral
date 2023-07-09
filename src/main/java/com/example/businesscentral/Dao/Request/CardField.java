package com.example.businesscentral.Dao.Request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Time;

@Data
@Accessors(chain = true)
public class CardField {

    private String Type;
    private Object Value;
}
