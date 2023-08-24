package com.example.businesscentral.Dao.Request;

import com.example.businesscentral.Dao.Enum.DataType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CardField {
    @JsonProperty("type")
    private DataType type;
    @JsonProperty("value")
    private Object value;
}
