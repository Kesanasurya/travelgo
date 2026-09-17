package com.travelgo.service;
import com.travelgo.dto.HotelRequest;
import com.travelgo.entity.*;
import com.travelgo.exception.ResourceNotFoundException;
import com.travelgo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class HotelService {
 private final HotelRepository hotels; private final DestinationRepository destinations;
 public List<Hotel> all(){return hotels.findAll();} public Hotel one(Long id){return hotels.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found"));}
 public Hotel save(HotelRequest r){return hotels.save(map(new Hotel(),r));}
 public Hotel update(Long id,HotelRequest r){var h=one(id);return hotels.save(map(h,r));}
 private Hotel map(Hotel h,HotelRequest r){h.setName(r.name());h.setDestination(destinations.findById(r.destinationId()).orElseThrow(()->new ResourceNotFoundException("Destination not found")));h.setDescription(r.description());h.setPricePerNight(r.pricePerNight());h.setRating(r.rating());h.setImageUrl(r.imageUrl());h.setLocation(r.location());h.setAmenities(r.amenities());return h;}
 public void delete(Long id){hotels.delete(one(id));}
}
