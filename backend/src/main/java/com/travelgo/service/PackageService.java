package com.travelgo.service;
import com.travelgo.dto.PackageRequest;
import com.travelgo.entity.*;
import com.travelgo.exception.ResourceNotFoundException;
import com.travelgo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class PackageService {
 private final TravelPackageRepository packages; private final DestinationRepository destinations;
 public List<TravelPackage> all(){return packages.findAll();} public TravelPackage one(Long id){return packages.findById(id).orElseThrow(()->new ResourceNotFoundException("Package not found"));}
 public TravelPackage save(PackageRequest r){return packages.save(map(new TravelPackage(),r));}
 public TravelPackage update(Long id,PackageRequest r){return packages.save(map(one(id),r));}
 private TravelPackage map(TravelPackage p,PackageRequest r){p.setName(r.name());p.setDestination(destinations.findById(r.destinationId()).orElseThrow(()->new ResourceNotFoundException("Destination not found")));p.setDescription(r.description());p.setDuration(r.duration());p.setPrice(r.price());p.setImageUrl(r.imageUrl());p.setIncludedServices(r.includedServices());return p;}
 public void delete(Long id){packages.delete(one(id));}
}
