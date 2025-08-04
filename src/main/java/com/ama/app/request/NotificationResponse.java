package com.ama.app.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Notification Response DTO")
public class NotificationResponse {
    @Schema(description = "Application Error Code", maxLength = 20)
    private String error_code;
    @Schema(description = "Error description based on error code", maxLength = 100)
    private String error_text;
    @Schema(description = "The inputted notify id for further communication")
    private int notifyId;

}
