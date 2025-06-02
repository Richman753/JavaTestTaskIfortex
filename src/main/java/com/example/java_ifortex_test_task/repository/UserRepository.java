package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select users.* from users " +
            "join sessions on users.id = sessions.user_id " +
            "where users.id in (select user_id from sessions where device_type = :#{#type?.getCode()} group by user_id having count(user_id) > 0) " +
            "group by users.id " +
            "order by max(sessions.started_at_utc) desc", nativeQuery = true)
    List<User> getUsersWithAtLeastOneMobileSession(@Param("type") DeviceType deviceType);

    @Query(value = "select users.* from users " +
            "join sessions on users.id = sessions.user_id " +
            "group by users.id " +
            "order by count(sessions.id) desc " +
            "limit 1", nativeQuery = true)
    User getUserWithMostSessions();
}
