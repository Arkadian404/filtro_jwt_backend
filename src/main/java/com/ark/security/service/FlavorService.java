package com.ark.security.service;

import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
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
        return flavorRepository.findById(id).orElseThrow(()-> new NotFoundException("Không tìm thấy loại hương vị: "+ id));
    }

    public List<Flavor> getAllFlavors() {
        List<Flavor> flavors = flavorRepository.findAll();
        if(flavors.isEmpty()){
            throw new NotFoundException("Không có loại hương vị nào");
        }
        return flavors;
    }

    public boolean existsFlavorByName(String name){
        return flavorRepository.existsFlavorByName(name);
    }

    public void saveFlavor(Flavor flavor) {
        if(existsFlavorByName(flavor.getName())) {
            throw new DuplicateException("Loại hương vị đã tồn tại");
        }
        flavorRepository.save(flavor);
    }

    public void updateFlavor(int id, Flavor flavor){
        Flavor oldFlavor = getFlavorById(id);
        if(flavor == null){
            throw new NullException("Không được để trống");
        }
        if (oldFlavor != null) {
            oldFlavor.setName(flavor.getName());
            oldFlavor.setDescription(flavor.getDescription());
            oldFlavor.setStatus(flavor.getStatus());
            flavorRepository.save(oldFlavor);
        }else{
            throw new NotFoundException("Không tìm thấy loại hương vị: "+ id);
        }
    }

    public void deleteFlavor(int id) {
        Flavor flavor = getFlavorById(id);
        if(flavor == null){
            throw new NotFoundException("Không tìm thấy loại hương vị: "+ id);
        }
        flavorRepository.deleteById(id);
    }
}
