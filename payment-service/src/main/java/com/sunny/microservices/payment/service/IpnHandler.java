package com.sunny.microservices.payment.service;

import com.sunny.microservices.payment.dto.response.IpnResponse;

import java.util.Map;
public interface IpnHandler {
    IpnResponse process(Map<String, String> params);
}
