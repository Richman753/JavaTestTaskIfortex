package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(value = "Select * from sessions where device_type = :#{#type?.getCode()} order by started_at_utc asc limit 1", nativeQuery = true)
    Session getFirstDesktopSession(@Param("type") DeviceType deviceType);

    @Query(value = "select sessions.* from sessions " +
            "join users on sessions.user_id = users.id " +
            "where users.deleted = false " +
            "and sessions.ended_at_utc < ?1 " +
            "order by sessions.started_at_utc desc", nativeQuery = true)
    List<Session> getSessionsFromActiveUsersEndedBefore2025(LocalDateTime endDate);
}