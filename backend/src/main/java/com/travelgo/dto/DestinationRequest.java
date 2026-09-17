package com.travelgo.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record DestinationRequest(@NotBlank String name, @NotBlank String country, String description, String imageUrl, @NotNull @Positive BigDecimal startingPrice) { }
