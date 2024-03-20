package com.swifttech.util;

import com.swifttech.model.Status;
import com.swifttech.model.User;
import com.swifttech.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class BlockTImeResetTask {
    private final ScheduledExecutorService scheduler= Executors.newScheduledThreadPool(1);

    private UserRepo userRepo;
    public void BlockTimeResetTask(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public void start() {
        scheduler.scheduleAtFixedRate(this::resetBlockTime, 0, 5, TimeUnit.MINUTES);
    }

    private void resetBlockTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<User> blockedUsers = userRepo.findByStatusAndBlockEndTimeBefore(Status.BLOCKED, currentTime);

        for (User user : blockedUsers) {
            user.setStatus(Status.ACTIVE);
            user.setFailedAttempts(0);
            user.setBlockStartTime(null);
            user.setBlockEndTime(null);
            userRepo.save(user);
        }
    }
}
