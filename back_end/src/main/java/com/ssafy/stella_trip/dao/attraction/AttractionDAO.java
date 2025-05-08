package com.ssafy.stella_trip.dao.attraction;

import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttractionDAO {
    AttractionDTO getAttractionById(int attractionId);
    List<AttractionDTO> getAttractionsBySidoCode(int sidoCode);
    List<AttractionDTO> getAttractionsBySidoAndGugunCode(int sidoCode, int gugunCode);
    List<AttractionDTO> getAttractionsByContentTypeId(int contentTypeId);
}
