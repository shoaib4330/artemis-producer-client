package com.kashegypt.gateway.jms.dto;

import com.kashegypt.gateway.jms.DestinationType;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MessageRequest {
    private String jmsType;
    private Object message;
    private DestinationType destination;
}
