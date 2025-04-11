package com.gtel.srpingtutorial;

import com.gtel.srpingtutorial.api.UserController;
import com.gtel.srpingtutorial.exception.ApplicationException;
import com.gtel.srpingtutorial.model.request.RegisterRequest;
import com.gtel.srpingtutorial.model.response.RegisterResponse;
import com.gtel.srpingtutorial.redis.entities.RegisterUserEntity;
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

public class UserRegisterApiTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0909123456");
        request.setPassword("Abcd1234");

        RegisterResponse response = new RegisterResponse();
        response.setTransactionId("tx123");

        when(userService.registerUser(any())).thenReturn(response);

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("tx123", Objects.requireNonNull(result.getBody()).getTransactionId());
    }

    @Test
    void testRegisterMissingPhone() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPassword("Abcd1234");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "phoneNumber is invalid"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testRegisterMissingPassword() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0909123456");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "password is invalid"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testRegisterInvalidPasswordFormat() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0909123456");
        request.setPassword("abc123");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "password không đúng định dạng"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testRegisterPhoneContainsLetters() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("09abc56789");
        request.setPassword("Abcd1234");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "số điện thoại không hợp lệ"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testRegisterPhoneWithWhitespace() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber(" 0987654321 ");
        request.setPassword("Abcd1234");

        RegisterResponse response = new RegisterResponse();
        response.setTransactionId("tx123");

        when(userService.registerUser(any())).thenReturn(response);

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testRegisterThrowsFromRepository() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0909123456");
        request.setPassword("Abcd1234");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Database error"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testRegisterOtpGenFails() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0909123456");
        request.setPassword("Abcd1234");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Lỗi sinh OTP"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testRegisterSqlInjectionInPhone() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("abc' OR 1=1 --");
        request.setPassword("Abcd1234");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "số điện thoại không hợp lệ"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void testRegisterXssInPassword() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setPhoneNumber("0909123456");
        request.setPassword("<script>alert(1)</script>");

        when(userService.registerUser(any())).thenThrow(new ApplicationException(ERROR_CODE.INVALID_REQUEST, "password không đúng định dạng"));

        ResponseEntity<RegisterResponse> result = userController.register(request);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}