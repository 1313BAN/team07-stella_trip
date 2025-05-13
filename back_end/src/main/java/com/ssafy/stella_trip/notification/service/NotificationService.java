package com.ssafy.stella_trip.notification.service;

import com.ssafy.stella_trip.notification.dto.NotificationDTO;
import com.ssafy.stella_trip.notification.dto.response.NotificationDetailResponseDTO;
import com.ssafy.stella_trip.notification.dto.response.NotificationResponseDTO;
import com.ssafy.stella_trip.notification.exception.NotificationNotFoundException;
import com.ssafy.stella_trip.common.dto.PageDTO;
import com.ssafy.stella_trip.common.util.PaginationUtils;
import com.ssafy.stella_trip.dao.notification.NotificationDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationDAO notificationDAO;

    public PageDTO<NotificationResponseDTO> getNotificationList(int page, int size) {
        return PaginationUtils.getPagedResult(
                page,
                size,
                notificationDAO::getNotificationCount,
                (offset, pageSize) -> notificationDAO.getNotifications(pageSize, offset),
                this::convertToNotificationResponseDTO
        );
    }

    public NotificationDetailResponseDTO getNotificationDetail(int notificationId) {
        NotificationDTO notification = notificationDAO.getNotificationDetail(notificationId);
        if (notification == null) {
            throw new NotificationNotFoundException("해당 Id의 공지사항이 존재하지 않습니다. notificationId: " + notificationId);
        }
        return convertToNotificationDetailResponseDTO(notification);
    }

    private NotificationResponseDTO convertToNotificationResponseDTO(NotificationDTO notification){
        return NotificationResponseDTO.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getModifiedAt())
                .build();
    }

    private NotificationDetailResponseDTO convertToNotificationDetailResponseDTO(NotificationDTO notification){
        return NotificationDetailResponseDTO.builder()
                .notificationId(notification.getNotificationId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getModifiedAt())
                .build();
    }
}
