package com.swifttech.repo;

import com.swifttech.model.Status;
import com.swifttech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByStatusAndBlockEndTimeBefore(Status status, LocalDateTime currentTime);
}
