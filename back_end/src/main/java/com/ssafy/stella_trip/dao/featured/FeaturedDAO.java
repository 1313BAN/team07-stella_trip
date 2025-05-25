package com.ssafy.stella_trip.dao.featured;

import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import com.ssafy.stella_trip.plan.dto.TagDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeaturedDAO {
    public List<AttractionDTO> getFeaturedAttractionsByContentType(@Param("contentTypeId") int contentTypeId);
    public List<TagDTO> getFeaturedTags();
}
