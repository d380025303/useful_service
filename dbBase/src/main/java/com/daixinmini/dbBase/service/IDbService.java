package com.daixinmini.dbBase.service;


public interface IDbService {
    <T> int delete(T t, Class clazz);

    <T> T save(T t, Class clazz);
}