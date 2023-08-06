package com.example.businesscentral.Dao.Request;

import com.example.businesscentral.Dao.Enum.DataType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Time;

@Data
@Accessors(chain = true)
public class CardField {

    private DataType Type;
    private Object Value;
}
