package com.travelgo.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record PackageRequest(@NotBlank String name,@NotNull Long destinationId,String description,@NotBlank String duration,@NotNull @Positive BigDecimal price,String imageUrl,String includedServices){}
