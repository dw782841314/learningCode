package com.hdkj.modules.JW.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ApiService {
    String query(String json) throws JsonProcessingException;
}
