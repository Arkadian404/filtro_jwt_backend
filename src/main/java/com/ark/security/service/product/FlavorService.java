package com.ark.security.service.product;

import com.ark.security.dto.FlavorDto;
import com.ark.security.exception.DuplicateException;
import com.ark.security.exception.NotFoundException;
import com.ark.security.exception.NullException;
import com.ark.security.models.product.Flavor;
import lombok.RequiredArgsConstructor;
import com.ark.security.repository.product.FlavorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlavorService {

    private final FlavorRepository flavorRepository;
    private final String NOT_FOUND = "Không tìm thấy loại hương vị nào: ";
    private final String EMPTY = "Không có loại hương vị nào";
    private final String DUPLICATE = "Loại hương vị đã tồn tại";
    private final String BLANK = "Không được để trống";

    public Flavor getFlavorById(int id) {
        return flavorRepository.findById(id).orElseThrow(()-> new NotFoundException(NOT_FOUND+ id));
    }

    public List<Flavor> getAllFlavors() {
        List<Flavor> flavors = flavorRepository.findAll();
        if(flavors.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return flavors;
    }

    public List<FlavorDto> getAllFlavorsDto() {
        List<FlavorDto> flavors = flavorRepository.findAll()
                .stream()
                .map(Flavor::convertToDto)
                .toList();
        if(flavors.isEmpty()){
            throw new NotFoundException(EMPTY);
        }
        return flavors;
    }

    public boolean existsFlavorByName(String name){
        return flavorRepository.existsFlavorByName(name);
    }

    public void saveFlavor(Flavor flavor) {
        if(existsFlavorByName(flavor.getName())) {
            throw new DuplicateException(DUPLICATE);
        }
        flavorRepository.save(flavor);
    }

    public void updateFlavor(int id, Flavor flavor){
        Flavor oldFlavor = getFlavorById(id);
        if(flavor == null){
            throw new NullException(BLANK);
        }
        if (oldFlavor != null) {
            oldFlavor.setName(flavor.getName());
            oldFlavor.setDescription(flavor.getDescription());
            oldFlavor.setStatus(flavor.getStatus());
            flavorRepository.save(oldFlavor);
        }else{
            throw new NotFoundException(NOT_FOUND+ id);
        }
    }

    public void deleteFlavor(int id) {
        Flavor flavor = getFlavorById(id);
        if(flavor == null){
            throw new NotFoundException(NOT_FOUND+ id);
        }
        flavorRepository.deleteById(id);
    }
}
