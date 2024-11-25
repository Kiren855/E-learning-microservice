package com.sunny.microservices.basedomain.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meta {

    private String requestId;

    private String status;

    private String message;

    private String serviceId;

    private Collection<ApiError> errors;

    private Map<String, Object> extraMeta;

}
