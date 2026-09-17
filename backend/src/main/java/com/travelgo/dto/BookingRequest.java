package com.travelgo.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record BookingRequest(@NotNull Long packageId, @NotBlank String roomType, @NotNull @FutureOrPresent LocalDate travelDate, @NotNull @Min(1) @Max(20) Integer guests) { }
