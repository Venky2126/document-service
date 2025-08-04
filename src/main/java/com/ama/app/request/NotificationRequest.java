package com.ama.app.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationRequest {

    // Primitive types (mandatory)
    @NotNull(message = "byteValue is required")
    @Schema(description = "Primitive byte value", required = true)
    private byte byteValue;

    @NotNull(message = "shortValue is required")
    @Schema(description = "Primitive short value", required = true)
    private short shortValue;

    @NotNull(message = "intValue is required")
    @Schema(description = "Primitive int value", required = true)
    private int intValue;

    @NotNull(message = "longValue is required")
    @Schema(description = "Primitive long value", required = true)
    private long longValue;

    @NotNull(message = "floatValue is required")
    @Schema(description = "Primitive float value", required = true)
    private float floatValue;

    @NotNull(message = "doubleValue is required")
    @Schema(description = "Primitive double value", required = true)
    private double doubleValue;

    @NotNull(message = "booleanValue is required")
    @Schema(description = "Primitive boolean value", required = true)
    private boolean booleanValue;

    @NotNull(message = "charValue is required")
    @Schema(description = "Primitive char value", required = true, maxLength = 1)
    private char charValue;

    // Wrapper types (optional)
    @Schema(description = "Byte wrapper value", required = false)
    private Byte byteWrapper;

    @Schema(description = "Short wrapper value", required = false)
    private Short shortWrapper;

    @Schema(description = "Integer wrapper value", required = false)
    private Integer integerWrapper;

    @Schema(description = "Long wrapper value", required = false)
    private Long longWrapper;

    @Schema(description = "Float wrapper value", required = false)
    private Float floatWrapper;

    @Schema(description = "Double wrapper value", required = false)
    private Double doubleWrapper;

    @Schema(description = "Boolean wrapper value", required = false)
    private Boolean booleanWrapper;

    @Schema(description = "Character wrapper value", required = false, maxLength = 1)
    private Character charWrapper;

    // String and other types
    @NotNull(message = "name is required")
    @Size(max = 100, message = "name max length is 100")
    @Schema(description = "Name of the request", required = true, maxLength = 100)
    private String name;

    @Schema(description = "Optional description", required = false, maxLength = 250)
    private String description;

    @Schema(description = "BigDecimal value", required = false)
    private BigDecimal amount;


}
