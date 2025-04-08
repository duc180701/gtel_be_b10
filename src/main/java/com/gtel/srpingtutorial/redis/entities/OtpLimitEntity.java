package com.gtel.srpingtutorial.redis.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("otp")
@Data
public class OtpLimitEntity {
    @Id
    private String phoneNumber;

    private int dailyOtpCounter = 0;
    @TimeToLive
    private long ttl;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDailyOtpCounter() {
        return dailyOtpCounter;
    }

    public void setDailyOtpCounter(int dailyOtpCounter) {
        this.dailyOtpCounter = dailyOtpCounter;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
