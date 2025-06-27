package com.ssafy.stella_trip.ai.service;

import com.ssafy.stella_trip.ai.config.OpenAIConfig;
import com.ssafy.stella_trip.ai.dto.*;
import com.ssafy.stella_trip.attraction.dto.AttractionDTO;
import com.ssafy.stella_trip.plan.dto.PlanDTO;
import com.ssafy.stella_trip.plan.dto.RouteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final OpenAIConfig config;

    private final RestTemplate restTemplate = new RestTemplate();

    public TarotResult sendMessageToGpt(PlanDTO planDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(config.getApiKey());

            String prompt = generate(planDTO);
            GPTMessageDTO userMessage = new GPTMessageDTO("user", prompt);
            GPTRequestDTO request = new GPTRequestDTO(config.getModel(), List.of(userMessage));

            HttpEntity<GPTRequestDTO> httpEntity = new HttpEntity<>(request, headers);

            ResponseEntity<GPTResponseDTO> response = restTemplate.postForEntity(
                    config.getUrl(),
                    httpEntity,
                    GPTResponseDTO.class
            );

            if (response.getBody() == null ||
                    response.getBody().getChoices() == null ||
                    response.getBody().getChoices().isEmpty()) {
                throw new RuntimeException("OpenAI API로부터 유효하지 않은 응답을 받았습니다");
            }

            return parse(response.getBody()
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent());
        } catch (RestClientException e) {
            // throw new RuntimeException("OpenAI API 호출 중 오류가 발생했습니다: " + e.getMessage(), e);

            // 기본 문구로 대체
            return new TarotResult("StellaTrip",
                "당신의 여정은 별빛에서 시작됩니다.\n" +
                        "관광지의 번잡함을 벗어나 밤하늘의 속삭임에 귀 기울이세요.\n" +
                        "과거의 별들이 전하는 이야기는 당신의 마음을 비추고,\n" +
                        "그 빛은 오늘의 당신을 더 먼 시간으로 이끌어 줍니다.\n" +
                        "별들이 이어준 길 위에서, 당신은 영원을 여행하는 별빛의 동반자입니다.",
                List.of("여행", "별자리", "타로카드"));
        }
    }

    private String generate(PlanDTO plan) {
        StringBuilder sb = new StringBuilder();

        sb.append("아래 여행 데이터를 기반으로 타로카드 메시지를 구성해줘.\n\n");
        sb.append("여행의 신비로운 패턴:\n");

        Map<String, Long> visitCount = plan.getRoutes().stream()
                .map(RouteDTO::getAttraction)
                .filter(Objects::nonNull)
                .map(AttractionDTO::getTitle)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        visitCount.entrySet().stream()
                .filter(e -> e.getValue() >= 2)
                .forEach(e -> sb.append("- ").append(e.getKey())
                        .append(" ").append(e.getValue())
                        .append("회 방문\n"));

        long areaCount = plan.getRoutes().stream()
                .map(RouteDTO::getAttraction)
                .filter(Objects::nonNull)
                .map(AttractionDTO::getSidoCode)
                .distinct()
                .count();

        if (areaCount > 1) {
            sb.append("- ").append(areaCount)
                    .append("개 지역 횡단 (완전한 도시 탐험 = 전체성 추구)\n");
        }

        List<String> contentFlow = plan.getRoutes().stream()
                .map(RouteDTO::getAttraction)
                .filter(Objects::nonNull)
                .map(a -> ContentType.getNameById(a.getContentTypeId()))
                .distinct()
                .toList();

        if (contentFlow.size() >= 2) {
            sb.append("- ").append(String.join(" → ", contentFlow))
                    .append("의 시간여행\n");
        }
        sb.append("\n이때 카드 이름은 타로카드같은 영단어로 해줘\n\n");
        sb.append("\n응답은 다음 형식을 꼭 지켜줘:\n\n");
        sb.append("🎴 카드 이름: [카드 이름]\n\n");
        sb.append("🔮 메시지:\n");
        sb.append("[운명적이고 감성적인 문장 3~4줄]\n\n");
        sb.append("✨ 키워드:\n");
        sb.append("[쉼표로 구분된 키워드 3~5개]\n\n");

        sb.append("요청 스타일:\n");
        sb.append("- \"당신은 ○○입니다\" 형태 포함\n");
        sb.append("- 여행을 운명적/영적 여정으로 해석\n");
        sb.append("- 장소 흐름/재방문 등 의미를 부여\n");

        return sb.toString();
    }

    private TarotResult parse(String gptResponse) {
        String cardName = extractCardName(gptResponse);
        String message = extractMessage(gptResponse);
        List<String> keywords = extractKeywords(gptResponse);

        return new TarotResult(cardName, message, keywords);
    }

    private String extractCardName(String text) {
        Matcher matcher = Pattern.compile("🎴 카드 이름:\\s*(.+)").matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "Unknown";
    }

    private String extractMessage(String text) {
        Matcher matcher = Pattern.compile("🔮 메시지:\\s*(.+?)(?=✨ 키워드:|$)", Pattern.DOTALL).matcher(text);
        return matcher.find() ? matcher.group(1).strip() : "";
    }

    private List<String> extractKeywords(String text) {
        Matcher matcher = Pattern.compile("✨ 키워드:\\s*(.+)").matcher(text);
        if (matcher.find()) {
            return Arrays.stream(matcher.group(1).split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
