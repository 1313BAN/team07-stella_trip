package com.ssafy.stella_trip.dao.stella;

import com.ssafy.stella_trip.stella.dto.StellaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StellaDAO {

    public Integer createStellaLink(@Param("stellaData") String stellaData,
                                @Param("stellaLink") String stellaLink,
                                @Param("planId") int planId);
    public StellaDTO getStellaLinkByStellaLink(@Param("stellaLink") String stellaLink);
}
