package com.travelgo.controller;
import com.travelgo.dto.DestinationRequest;
import com.travelgo.entity.Destination;
import com.travelgo.service.DestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/destinations") @RequiredArgsConstructor
public class DestinationController { private final DestinationService service;
 @GetMapping public List<Destination> all(){return service.all();} @GetMapping("/{id}") public Destination one(@PathVariable Long id){return service.one(id);}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public Destination create(@Valid @RequestBody DestinationRequest r){return service.save(r);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Destination update(@PathVariable Long id,@Valid @RequestBody DestinationRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id){service.delete(id);}
}
