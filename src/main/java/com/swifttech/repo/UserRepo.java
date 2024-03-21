package com.swifttech.repo;

import com.swifttech.model.Status;
import com.swifttech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);
//    User findByIdAndStatus(Long id,Status status);

}
