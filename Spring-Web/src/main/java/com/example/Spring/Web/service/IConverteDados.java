package com.example.Spring.Web.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
