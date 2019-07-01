package com.kashegypt.gateway.jms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MultiCastNotificationMessageDTO implements Serializable {
    private static final long serialVersionUID = -1L;

    @NotBlank(message = "templateName must not be null or blank")
    private String templateName;

    private Map<String, String> placeHolderDataForBody;
    private Map<String, String> placeHolderDataForTitle;

    @NotNull(message = "languageId must not be null")
    private Integer languageId;

    @NotNull(message = "notificationType must not be null")
    private int notificationType;

    @NotBlank(message = "topicName must not be null or blank")
    private String topicName;

    @NotNull(message = "createdTimestamp must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createdTimestamp;
}
