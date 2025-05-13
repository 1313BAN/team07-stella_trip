package com.ssafy.stella_trip.dao.admin;

import com.ssafy.stella_trip.admin.dto.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDAO {
    List<NotificationDTO> getNotifications(@Param("size") int size,
                                           @Param("page") int page);

    int getNotificationCount();

    NotificationDTO getNotificationDetail(@Param("notificationId") int notificationId);

    int insertNotification(@Param("title") String title,
                           @Param("content") String content,
                           @Param("userId") int userId);

    int updateNotification(@Param("notificationId") int notificationId,
                           @Param("title") String title,
                           @Param("content") String content);

    int deleteNotification(@Param("notificationId") int notificationId);
}
