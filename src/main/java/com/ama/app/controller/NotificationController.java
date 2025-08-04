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
import com.ama.app.request.NotificationResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

	@Operation(summary = "notification-service", description = "notification-service", tags = { "notification-service" })
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NotificationResponse> handleCreateNotification(
	        @RequestHeader(name = HEADER_CHANNEL_ID, required = true) String channelId,
	        @RequestHeader(name = HEADER_TRANSACTION_DATE_TIME, required = true) String transactionDateTime,
	        @RequestHeader(name = HEADER_TRANSACTION_TIME_ZONE, required = true) String transactionTimeZone,
	        @RequestHeader(name = HEADER_COUNTRY_OF_ORIGIN, required = true) String countryOfOrigin,
	        @RequestHeader(name = HEADER_TRANSACTION_ID, required = true) String transactionIdentifier,
	        @Valid @RequestBody NotificationRequest request) {

	    log.info("Inside NotificationController transactionIdentifier : {}", transactionIdentifier);


	    NotificationResponse response = new NotificationResponse();
	    response.setError_code("0"); // Example: success
	    response.setError_text("Success");
	    response.setNotifyId(request.getIntValue());
	    
	    return ResponseEntity.ok(response);
	}
}
