package com.ssafy.stella_trip.stella.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.stella_trip.ai.dto.TarotResult;
import com.ssafy.stella_trip.ai.service.OpenAIService;
import com.ssafy.stella_trip.dao.plan.PlanDAO;
import com.ssafy.stella_trip.dao.stella.StellaDAO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.exception.PlanNotFoundException;
import com.ssafy.stella_trip.plan.exception.UnauthorizedPlanAccessException;
import com.ssafy.stella_trip.security.dto.JwtUserInfo;
import com.ssafy.stella_trip.stella.dto.StellaDTO;
import com.ssafy.stella_trip.stella.dto.request.StellaRequestDTO;
import com.ssafy.stella_trip.stella.dto.response.StellaListResponseDTO;
import com.ssafy.stella_trip.stella.dto.response.StellaResponseDTO;
import com.ssafy.stella_trip.stella.exception.StellaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StellaService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PlanDAO planDAO;
    private final StellaDAO stellaDAO;
    private final OpenAIService openAIService;
    private final ObjectMapper objectMapper;


    public StellaResponseDTO createStellaLink(StellaRequestDTO stella, JwtUserInfo user) throws JsonProcessingException {
        PlanDTO planDTO = planDAO.getPlanById(stella.getPlanId(), 0);
        if (planDTO == null) {
            throw new PlanNotFoundException("해당 플랜이 존재하지 않습니다.:" + stella.getPlanId());
        }
        if (planDTO.getWriters().stream().noneMatch(writer -> writer.getUserId() == user.getUserId())) {
            throw new UnauthorizedPlanAccessException("해당 플랜에 대한 권한이 없습니다.");
        }

        String randomLink = UUID.randomUUID().toString();
        for (int i = 0; i < 10; i++) {
            if (stellaDAO.getStellaLinkByStellaLink(randomLink) == null) {
                // AI 응답 생성
                TarotResult aiResponse = openAIService.sendMessageToGpt(planDTO);
                String jsonString = objectMapper.writeValueAsString(aiResponse);

                stellaDAO.createStellaLink(stella.getStellaData(), randomLink, user.getUserId(), stella.getPlanId(), jsonString);

                StellaResponseDTO responseDTO = new StellaResponseDTO(stella.getPlanId(), user.getUserId(), stella.getStellaData(), randomLink, aiResponse);
                redisTemplate.opsForValue().set(randomLink, responseDTO, 7, TimeUnit.DAYS); // 1 day expiration
                return responseDTO;
            }
            randomLink = UUID.randomUUID().toString();
        }
        throw new RuntimeException("랜덤 링크 생성에 실패했습니다. 다시 시도해주세요.");
    }

    public StellaListResponseDTO getStellaByUserId(int userId) {
        List<StellaDTO> stella = stellaDAO.getStellaLinkByUserId(userId);

        List<StellaResponseDTO> responseDTO = stella.stream()
                .map(s -> {
                    try {
                        TarotResult aiResponse = objectMapper.readValue(s.getStellaAI(), TarotResult.class);
                        return new StellaResponseDTO(s.getPlanId(), s.getUserId(), s.getStellaData(), s.getStellaLink(), aiResponse);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("AI 응답 변환에 실패했습니다.", e);
                    }
                })
                .toList();

        return new StellaListResponseDTO(responseDTO);
    }

    public StellaResponseDTO getStella(String link) {
        Object cachedResponse = redisTemplate.opsForValue().get(link);
        if (cachedResponse instanceof StellaResponseDTO) {
            return (StellaResponseDTO) cachedResponse;
        }
        StellaDTO stella = stellaDAO.getStellaLinkByStellaLink(link);
        if (stella == null) {
            throw new StellaNotFoundException("해당 Stella 링크가 존재하지 않습니다: " + link);
        }
        // 캐시가 없으면 StellaDTO를 StellaResponseDTO로 변환
        try {
            TarotResult aiResponse = objectMapper.readValue(stella.getStellaAI(), TarotResult.class);
            StellaResponseDTO responseDTO = new StellaResponseDTO(stella.getPlanId(), stella.getUserId(), stella.getStellaData(), stella.getStellaLink(), aiResponse);
            redisTemplate.opsForValue().set(responseDTO.getStellaLink(), responseDTO, 7, TimeUnit.DAYS); // 1 day expiration
            return responseDTO;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("AI 응답 변환에 실패했습니다.", e);
        }
    }

}
