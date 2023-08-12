package com.example.businesscentral.Dao.Request;

import com.example.businesscentral.Dao.Enum.DataType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CardField {

    private DataType type;
    private Object value;
}
