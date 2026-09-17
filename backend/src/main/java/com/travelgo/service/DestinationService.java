package com.travelgo.service;
import com.travelgo.dto.DestinationRequest;
import com.travelgo.entity.Destination;
import com.travelgo.exception.ResourceNotFoundException;
import com.travelgo.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class DestinationService {
 private final DestinationRepository repo;
 public List<Destination> all(){return repo.findAll();}
 public Destination one(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Destination not found"));}
 public Destination save(DestinationRequest r){return repo.save(new Destination(null,r.name(),r.country(),r.description(),r.imageUrl(),r.startingPrice(),null,null));}
 public Destination update(Long id, DestinationRequest r){var d=one(id);d.setName(r.name());d.setCountry(r.country());d.setDescription(r.description());d.setImageUrl(r.imageUrl());d.setStartingPrice(r.startingPrice());return repo.save(d);}
 public void delete(Long id){repo.delete(one(id));}
}
