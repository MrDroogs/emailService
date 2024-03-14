package com.swifttech.repo;

import com.swifttech.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepo extends JpaRepository<Otp,Long> {
   Otp findByEmail (String email);
}
