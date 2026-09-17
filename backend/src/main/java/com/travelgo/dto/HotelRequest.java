package com.travelgo.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record HotelRequest(@NotBlank String name,@NotNull Long destinationId,String description,@NotNull @Positive BigDecimal pricePerNight,@NotNull @DecimalMin("0") @DecimalMax("5") BigDecimal rating,String imageUrl,String location,String amenities){}
