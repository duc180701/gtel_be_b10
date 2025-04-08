package com.gtel.srpingtutorial.redis.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@RedisHash("register_user")
public class RegisterUserEntity {

    @Id
    private String transactionId;

    private String otp;

    private long otpExpiredTime;

    private long otpResendTime;

    private int otpResendCount;

    private String phoneNumber;

    private String password;

    private int otpFail;

    @TimeToLive
    private long ttl;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public long getOtpExpiredTime() {
        return otpExpiredTime;
    }

    public void setOtpExpiredTime(long otpExpiredTime) {
        this.otpExpiredTime = otpExpiredTime;
    }

    public long getOtpResendTime() {
        return otpResendTime;
    }

    public void setOtpResendTime(long otpResendTime) {
        this.otpResendTime = otpResendTime;
    }

    public int getOtpResendCount() {
        return otpResendCount;
    }

    public void setOtpResendCount(int otpResendCount) {
        this.otpResendCount = otpResendCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOtpFail() {
        return otpFail;
    }

    public void setOtpFail(int otpFail) {
        this.otpFail = otpFail;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
