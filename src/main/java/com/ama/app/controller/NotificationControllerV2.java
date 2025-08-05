package com.ama.app.controller;


import static com.ama.app.constants.ApplicationConstants.HEADER_CHANNEL_ID;
import static com.ama.app.constants.ApplicationConstants.HEADER_COUNTRY_OF_ORIGIN;
import static com.ama.app.constants.ApplicationConstants.HEADER_TRANSACTION_DATE_TIME;
import static com.ama.app.constants.ApplicationConstants.HEADER_TRANSACTION_ID;
import static com.ama.app.constants.ApplicationConstants.HEADER_TRANSACTION_TIME_ZONE;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ama.app.request.NotificationRequest;
import com.ama.app.request.NotificationResponseEntity;
import com.ama.app.request.NotificationResponseV2;
import com.ama.app.request.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/notification")
public class NotificationControllerV2 {

	@Operation(summary = "notification-service", description = "notification-service", tags = { "notification-service" })
	@PostMapping(value = "/response", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NotificationResponseV2> handleCreateNotification(
	        @RequestHeader(name = HEADER_CHANNEL_ID, required = true) String channelId,
	        @RequestHeader(name = HEADER_TRANSACTION_DATE_TIME, required = true) String transactionDateTime,
	        @RequestHeader(name = HEADER_TRANSACTION_TIME_ZONE, required = true) String transactionTimeZone,
	        @RequestHeader(name = HEADER_COUNTRY_OF_ORIGIN, required = true) String countryOfOrigin,
	        @RequestHeader(name = HEADER_TRANSACTION_ID, required = true) String transactionIdentifier,
	        @Valid @RequestBody NotificationRequest request) {

	    log.info("Inside NotificationController transactionIdentifier : {}", transactionIdentifier);


	    NotificationResponseV2 response = new NotificationResponseV2();
	    ResponseStatus responseStatus=ResponseStatus.builder().error_code("0").error_text("").build();
	    
	    NotificationResponseEntity notificationResponseEntity=NotificationResponseEntity.builder()
	    		.address4("HYD")
	    		.build();
	    response.setResponseStatus(responseStatus);
	    response.setNotificationResponseEntity(notificationResponseEntity);
	    return ResponseEntity.ok(response);
	}
}
