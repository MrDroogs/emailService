package com.swifttech.repo;


import com.swifttech.model.Otp;
import com.swifttech.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepo extends JpaRepository<Otp,Long> {

    Otp findByStatus(Status status);
}
