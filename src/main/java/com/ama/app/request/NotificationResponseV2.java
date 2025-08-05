package com.ama.app.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Notification Response")
public class NotificationResponseV2 {
	private ResponseStatus responseStatus;
    private NotificationResponseEntity notificationResponseEntity;

}
