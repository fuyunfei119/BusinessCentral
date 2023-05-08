package com.example.businesscentral.Dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessCentralQuery<E> {

    BusinessCentralQuery<E> SetJoinCondition(E... field);

    List<String> GetJoinCondition();

    BusinessCentralQuery<E> SetDataItem(E... field);

    List<String> GetDataItem();

    <T> BusinessCentralQuery<E> GetDataSet(Class<T> classes);
}
