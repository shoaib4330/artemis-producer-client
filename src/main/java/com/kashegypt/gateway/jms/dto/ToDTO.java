package com.kashegypt.gateway.jms.dto;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ToDTO {
    private Long userId;
    private String name;
    private String email;
    private Map<String, String> additionalData;
}
