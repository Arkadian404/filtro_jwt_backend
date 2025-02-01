package com.ark.security.service.product;

import com.ark.security.dto.request.FlavorRequest;
import com.ark.security.dto.response.FlavorResponse;
import com.ark.security.exception.*;
import com.ark.security.mapper.FlavorMapper;
import com.ark.security.models.product.Flavor;
import lombok.RequiredArgsConstructor;
import com.ark.security.repository.product.FlavorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlavorService {
    private final FlavorRepository flavorRepository;
    private final FlavorMapper flavorMapper;

    public List<FlavorResponse> getAllFlavors(){
        return flavorRepository.findAll()
                .stream()
                .map(flavorMapper::toFlavorResponse)
                .toList();
    }


    public FlavorResponse getFlavorById(int id){
        return flavorMapper.toFlavorResponse(
                flavorRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLAVOR_NOT_FOUND))
        );
    }



    public FlavorResponse saveFlavor(FlavorRequest request){
        Flavor flavor = flavorMapper.toFlavor(request);
        return flavorMapper.toFlavorResponse(flavorRepository.save(flavor));
    }

    public FlavorResponse updateFlavor(int id, FlavorRequest request){
        Flavor flavor = flavorRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLAVOR_NOT_FOUND));
        flavorMapper.updateFlavor(flavor, request);
        return flavorMapper.toFlavorResponse(flavorRepository.save(flavor));
    }

    public void deleteFlavor(int id){
        Flavor flavor = flavorRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.FLAVOR_NOT_FOUND));
        flavorRepository.delete(flavor);
    }
}
