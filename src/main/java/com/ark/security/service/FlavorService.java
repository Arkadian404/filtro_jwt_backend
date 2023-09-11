package com.ark.security.service;

import com.ark.security.models.Flavor;
import lombok.RequiredArgsConstructor;
import com.ark.security.repository.FlavorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlavorService {

    private final FlavorRepository flavorRepository;

    public Flavor getFlavorById(int id) {
        return flavorRepository.findById(id).orElse(null);
    }

    public List<Flavor> getAllFlavors() {
        return flavorRepository.findAll();
    }

    public void saveFlavor(Flavor flavor) {
        flavorRepository.save(flavor);
    }

    public void updateFlavor(int id, Flavor flavor){
        Flavor oldFlavor = getFlavorById(id);
        if (oldFlavor != null) {
            oldFlavor.setName(flavor.getName());
            oldFlavor.setDescription(flavor.getDescription());
            oldFlavor.setStatus(flavor.getStatus());
            flavorRepository.save(oldFlavor);
        }
    }

    public void deleteFlavor(int id) {
        flavorRepository.deleteById(id);
    }


}
