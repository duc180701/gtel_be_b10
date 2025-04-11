package com.gtel.srpingtutorial.api;

import com.gtel.srpingtutorial.exception.ApplicationException;
import com.gtel.srpingtutorial.model.request.ConfirmOtpRegisterRequest;
import com.gtel.srpingtutorial.model.request.RegisterRequest;
import com.gtel.srpingtutorial.model.response.RegisterResponse;
import com.gtel.srpingtutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    UserService userService;
    // 1. register
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) throws ApplicationException {
        try {
            RegisterResponse response = userService.registerUser(registerRequest);
            return ResponseEntity.ok(response);
        } catch (ApplicationException ex) {
            return ResponseEntity.badRequest().body(new RegisterResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterResponse("Unexpected error"));
        }
    }

    @GetMapping("otp/resend/{transactionId}")
    public ResponseEntity<RegisterResponse> resendOtp(@PathVariable("transactionId") String transactionId) {
        try {
            RegisterResponse response = userService.resendOtp(transactionId);
            return ResponseEntity.ok(response);
        } catch (ApplicationException ex) {
            return ResponseEntity.badRequest().body(new RegisterResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RegisterResponse("Unexpected error"));
        }
    }

    @PostMapping("otp/confirm")
    public ResponseEntity<Void> confirmRegisterOtp(@RequestBody ConfirmOtpRegisterRequest request){
        try {
            userService.confirmRegisterOtp(request);
            return ResponseEntity.ok().build();
        } catch (ApplicationException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}