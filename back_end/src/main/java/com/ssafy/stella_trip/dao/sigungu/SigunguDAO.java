package com.ssafy.stella_trip.dao.sigungu;

import com.ssafy.stella_trip.attraction.dto.SigunguDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SigunguDAO {
    List<SigunguDTO> getSigunguList();
}
