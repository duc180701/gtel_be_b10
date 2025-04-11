package com.gtel.srpingtutorial.model.request;

import lombok.Data;

@Data
public class ConfirmOtpRegisterRequest {
    private String transactionId;
    private String otp;

    public ConfirmOtpRegisterRequest(String abc123, String number) {
    }

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
}
