package com.ssafy.stella_trip.plan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.plan.dto.ConstellationDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConstellationService {

    private final int canvasSize = 500;
    private final PlanDAO planDAO;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ConstellationDTO convertJsonToConstellation(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, ConstellationDTO.class);
    }

    public String convertConstellationToJson(ConstellationDTO constellationDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(constellationDTO);
    }

    public ConstellationDTO updateStella(int planId) throws JsonProcessingException {
        List<RouteDTO> routes = planDAO.getRoutesByPlanId(planId);
        // 순서 정렬
        routes.sort((o1, o2) -> {
            if (o1.getDayIndex() == o2.getDayIndex()) {
                return Integer.compare(o1.getOrder(), o2.getOrder());
            }
            return Integer.compare(o1.getDayIndex(), o2.getDayIndex());
        });

        List<AttractionDTO> places = routes.stream().map(RouteDTO::getAttraction).toList();

        if(places.isEmpty()) {
            String json = convertConstellationToJson(new ConstellationDTO());
            planDAO.updateStella(planId, json);
            return new ConstellationDTO();
        }

        double centerLat = places.stream().mapToDouble(AttractionDTO::getLatitude).average().orElse(0);
        double centerLon = places.stream().mapToDouble(AttractionDTO::getLongitude).average().orElse(0);

        // 1. 노드 생성 (위도/경도 → 평면 좌표 정규화)
        double maxDx = 0, maxDy = 0;
        List<double[]> rawCoords = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (AttractionDTO place : places) {
            double dx = (place.getLongitude() - centerLon) * Math.cos(Math.toRadians(centerLat));
            double dy = place.getLatitude() - centerLat;
            rawCoords.add(new double[]{dx, dy});
            names.add(place.getTitle());
            maxDx = Math.max(maxDx, Math.abs(dx));
            maxDy = Math.max(maxDy, Math.abs(dy));
        }

        double maxRange = Math.max(maxDx, maxDy);
        int canvasSize = 500;
        ConstellationDTO constellationDTO = new ConstellationDTO();

        for (int i = 0; i < places.size(); i++) {
            String name = names.get(i);
            double[] coord = rawCoords.get(i);
            int x = (int) ((coord[0] / maxRange) * canvasSize / 2 + (double) canvasSize / 2);
            int y = (int) ((coord[1] / maxRange) * canvasSize / 2 + (double) canvasSize / 2);
            constellationDTO.addNode(name, x, y);
        }

        // 2. 엣지 생성 (인접한 노드 연결)
        for (int i = 1; i < places.size(); i++) {
            constellationDTO.addEdge(i - 1, i);
        }

        // 3. 노드와 엣지의 위치를 JSON으로 변환
        String json = convertConstellationToJson(constellationDTO);

        // 4. JSON을 DB에 저장
        planDAO.updateStella(planId, json);

        return constellationDTO;
    }
}
