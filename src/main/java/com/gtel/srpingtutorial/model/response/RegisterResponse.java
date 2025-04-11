package com.gtel.srpingtutorial.model.response;

import com.gtel.srpingtutorial.redis.entities.RegisterUserEntity;
import lombok.Data;

@Data
public class RegisterResponse {
    private String transactionId;

    private long otpExpiredTime;

    private long resendOtpTime;

    private String otp;

    private String error;

    public RegisterResponse() {
    }

    public RegisterResponse(String error){
        this.error = error;
    }

    public RegisterResponse(RegisterUserEntity entity){
        this.transactionId = entity.getTransactionId();
        this.otpExpiredTime = entity.getOtpExpiredTime() - System.currentTimeMillis()/1000;
        this.resendOtpTime = entity.getOtpResendTime() - System.currentTimeMillis()/1000;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getOtpExpiredTime() {
        return otpExpiredTime;
    }

    public void setOtpExpiredTime(long otpExpiredTime) {
        this.otpExpiredTime = otpExpiredTime;
    }

    public long getResendOtpTime() {
        return resendOtpTime;
    }

    public void setResendOtpTime(long resendOtpTime) {
        this.resendOtpTime = resendOtpTime;
    }
}
