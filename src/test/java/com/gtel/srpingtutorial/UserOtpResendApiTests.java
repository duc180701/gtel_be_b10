package com.gtel.srpingtutorial;

import com.gtel.srpingtutorial.api.UserController;
import com.gtel.srpingtutorial.exception.ApplicationException;
import com.gtel.srpingtutorial.model.response.RegisterResponse;
import com.gtel.srpingtutorial.service.UserService;
import com.gtel.srpingtutorial.utils.ERROR_CODE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserOtpResendApiTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOtpResendSuccess() {
        String transactionId = "abc123";

        RegisterResponse response = new RegisterResponse();
        response.setTransactionId(transactionId);

        when(userService.resendOtp(transactionId)).thenReturn(response);

        ResponseEntity<RegisterResponse> result = userController.resendOtp(transactionId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(transactionId, Objects.requireNonNull(result.getBody()).getTransactionId());
    }

    @Test
    void testOtpResendTooSoon() {
        String transactionId = "abc123";

        when(userService.resendOtp(transactionId))
                .thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP sent recently"));

        ResponseEntity<RegisterResponse> result = userController.resendOtp(transactionId);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testOtpResendTransactionNotFound() {
        String transactionId = "random123";

        when(userService.resendOtp(transactionId))
                .thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Transaction ID not found"));

        ResponseEntity<RegisterResponse> result = userController.resendOtp(transactionId);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testOtpResendRedisTimeout() {
        String transactionId = "abc123";

        when(userService.resendOtp(transactionId))
                .thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Redis timeout"));

        ResponseEntity<RegisterResponse> result = userController.resendOtp(transactionId);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testOtpResendMissingOtpResendTime() {
        String transactionId = "abc123";

        when(userService.resendOtp(transactionId))
                .thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Missing otpResendTime"));

        ResponseEntity<RegisterResponse> result = userController.resendOtp(transactionId);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testOtpResendOtpGenFails() {
        String transactionId = "abc123";

        when(userService.resendOtp(transactionId))
                .thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP gen lỗi"));

        ResponseEntity<RegisterResponse> result = userController.resendOtp(transactionId);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testOtpResendTimeEqualsCurrent() {
        String transactionId = "abc123";

        RegisterResponse response = new RegisterResponse();
        response.setTransactionId(transactionId);

        when(userService.resendOtp(transactionId)).thenReturn(response);

        ResponseEntity<RegisterResponse> result = userController.resendOtp(transactionId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}