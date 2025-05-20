package com.ssafy.stella_trip.attraction.service;


import com.ssafy.stella_trip.attraction.dto.SigunguDTO;
import com.ssafy.stella_trip.attraction.dto.response.GugunResponseDTO;
import com.ssafy.stella_trip.attraction.dto.response.SidoResponseDTO;
import com.ssafy.stella_trip.attraction.dto.response.SigunguResponseDTO;
import com.ssafy.stella_trip.dao.sigungu.SigunguDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SigunguService {

    private final SigunguDAO sigunguDAO;
    private final RedisTemplate<String, Object> redisTemplate;

    public SigunguResponseDTO getSigungu() {

        if (redisTemplate.hasKey("sigungu")) {
            Object cachedValue = redisTemplate.opsForValue().get("sigungu");
            if (cachedValue instanceof SigunguResponseDTO) {
                return (SigunguResponseDTO) cachedValue;
            }
        }

        List<SigunguDTO> sigunguList = sigunguDAO.getSigunguList();
        Map<Integer, SidoResponseDTO> sidoMap = new HashMap<>();
        sigunguList.forEach(sigunguDTO -> {
            if(!sidoMap.containsKey(sigunguDTO.getSidoCode())){
                sidoMap.put(sigunguDTO.getSidoCode(), new SidoResponseDTO(sigunguDTO.getSidoCode(), sigunguDTO.getSidoName(), new ArrayList<>()));
            }
            SidoResponseDTO sidoResponseDTO = sidoMap.get(sigunguDTO.getSidoCode());
            sidoResponseDTO.getGugunList().add(new GugunResponseDTO(sigunguDTO.getSidoCode(), sigunguDTO.getGugunCode(), sigunguDTO.getGugunName()));
        });
        redisTemplate.opsForValue().set("sigungu", sigunguList);

        return new SigunguResponseDTO(new ArrayList<>(sidoMap.values()));
    }

}
