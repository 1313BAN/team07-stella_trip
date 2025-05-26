package com.ssafy.stella_trip.dao.stella;

import com.ssafy.stella_trip.stella.dto.StellaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StellaDAO {

    public Integer createStellaLink(@Param("stellaData") String stellaData,
                                @Param("stellaLink") String stellaLink,
                                @Param("userId") int userId,
                                @Param("planId") int planId,
                                @Param("stellaAI") String stellaAI);
    public StellaDTO getStellaLinkByStellaLink(@Param("stellaLink") String stellaLink);
    public List<StellaDTO> getStellaLinkByUserId(@Param("userId") int userId);
}
