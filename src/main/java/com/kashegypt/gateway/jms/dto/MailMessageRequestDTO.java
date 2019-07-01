package com.kashegypt.gateway.jms.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MailMessageRequestDTO {
    private Long languageId;
    private String templateName;
    private String from;
    private List<ToDTO> to;
    private ToDTO cc;
    private ToDTO bcc;
}
