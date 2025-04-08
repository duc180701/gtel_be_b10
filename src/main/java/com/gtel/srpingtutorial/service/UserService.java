package com.gtel.srpingtutorial.service;

import com.gtel.srpingtutorial.domains.OtpDomain;
import com.gtel.srpingtutorial.entity.UserEntity;
import com.gtel.srpingtutorial.exception.ApplicationException;
import com.gtel.srpingtutorial.model.request.ConfirmOtpRegisterRequest;
import com.gtel.srpingtutorial.model.request.RegisterRequest;
import com.gtel.srpingtutorial.model.response.RegisterResponse;
import com.gtel.srpingtutorial.redis.entities.RegisterUserEntity;
import com.gtel.srpingtutorial.redis.repository.RegisterUserRedisRepository;
import com.gtel.srpingtutorial.repository.UserRepository;
import com.gtel.srpingtutorial.utils.ERROR_CODE;
import com.gtel.srpingtutorial.utils.PhoneNumberUtils;
import com.gtel.srpingtutorial.utils.USER_STATUS;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Slf4j
@Service
public class UserService  extends BaseService{

    private final OtpDomain otpDomain;

    private final UserRepository userRepository;

    private final RegisterUserRedisRepository registerUserRedisRepository;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(OtpDomain otpDomain, UserRepository userRepository, RegisterUserRedisRepository registerUserRedisRepository) {
        this.otpDomain = otpDomain;
        this.userRepository = userRepository;
        this.registerUserRedisRepository = registerUserRedisRepository;
    }

    public RegisterResponse registerUser(RegisterRequest request) throws ApplicationException {

        //validate request
        this.validateUserRegisterRequest(request);

        // check user exist on db
        String phoneNumber = PhoneNumberUtils.validatePhoneNumber(request.getPhoneNumber());
        log.info("[registerUser] - user register with phone {} START", phoneNumber);

        UserEntity userEntity = userRepository.findByPhoneNumber(phoneNumber);

        if (userEntity != null){
            log.info("[registerUser] request fail : user already exists with phone {}", phoneNumber);
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "PhoneNumber is already exists");
        }
        // otp gen
        RegisterUserEntity otpEntity = otpDomain.genOtpWhenUserRegister(phoneNumber, request.getPassword());
        //log.debug("[registerUser] - user register with phone entity {} ", otpEntity);
        log.info("[registerUser] - user register with phone {} DONE", request.getPhoneNumber());
        return new RegisterResponse(otpEntity);
    }

    protected void validateUserRegisterRequest(RegisterRequest request) throws ApplicationException {
        if (StringUtils.isBlank(request.getPhoneNumber())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER , "phoneNumber is invalid");
        }


        if (StringUtils.isBlank(request.getPassword())) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER , "password is invalid");
        }

        com.gtel.srpingtutorial.utils.StringUtils.validatePassword(request.getPassword());
    }

    public RegisterResponse resendOtp(String transactionId) throws ApplicationException {

        log.info("[resendOtp] - Start processing with transactionId: {}", transactionId);

        // get user info from redis by transactionId
        Optional<RegisterUserEntity> optionalRegisterUser = registerUserRedisRepository.findById(transactionId);
        if (optionalRegisterUser.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Transaction ID not found");
        }

        RegisterUserEntity registerUserEntity = optionalRegisterUser.get();
        long currentUnixTimestamp = System.currentTimeMillis() / 1000;

        // check allowedResendTime to resend otp
        long allowedResendTime = registerUserEntity.getOtpResendTime();
        if (currentUnixTimestamp < allowedResendTime) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Please wait before requesting OTP resubmission");
        }

        // check max resend tmp
        int maxResendAttempts = 5;
        if (registerUserEntity.getOtpResendCount() >= maxResendAttempts) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Exceeded the number of OTP resubmissions allowed");
        }

        // gen otp
        String newlyGeneratedOtp = otpDomain.genOtp();
        registerUserEntity.setOtp(newlyGeneratedOtp);

        // check resend cooldown
        long resendCooldownSeconds = 60;
        registerUserEntity.setOtpResendTime(currentUnixTimestamp + resendCooldownSeconds);

        // +1 resend
        int updatedResendCount = registerUserEntity.getOtpResendCount() + 1;
        registerUserEntity.setOtpResendCount(updatedResendCount);

        registerUserRedisRepository.save(registerUserEntity);
        log.info("[resendOtp] - Successfully resend OTP for transactionId: {}", transactionId);
        log.info("[newOtp] - New OTP sent: {}", newlyGeneratedOtp);
        return new RegisterResponse(registerUserEntity);
    }

    public void confirmRegisterOtp(ConfirmOtpRegisterRequest request) throws ApplicationException {

        String transactionId = request.getTransactionId();
        String inputOtp = request.getOtp();

        log.info("[confirmRegisterOtp] - Start processing with transactionId: {}", transactionId);

        // get user info from redis by transactionId
        Optional<RegisterUserEntity> optionalRegisterUser = registerUserRedisRepository.findById(transactionId);
        if (optionalRegisterUser.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Transaction ID not found");
        }

        RegisterUserEntity registerUserEntity = optionalRegisterUser.get();
        long currentUnixTimestamp = System.currentTimeMillis() / 1000;
        long otpExpirationTimestamp = registerUserEntity.getOtpExpiredTime();

        // check expired otp
        if (currentUnixTimestamp > otpExpirationTimestamp) {
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Expired OTP code");
        }

        String expectedOtp = registerUserEntity.getOtp();

        // compare otp
        if (!expectedOtp.equals(inputOtp)) {
            int failedAttempts = registerUserEntity.getOtpFail() + 1;
            registerUserEntity.setOtpFail(failedAttempts);

            // update incorrect count to redis
            registerUserRedisRepository.save(registerUserEntity);

            // lock when > 3
            if (failedAttempts >= 3) {
                throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "Wrong OTP code more than 3 times\n Please try again later");
            }
            throw new ApplicationException(ERROR_CODE.INVALID_REQUEST, "OTP Incorrect");
        }

        UserEntity newUser = new UserEntity();
        newUser.setPhoneNumber(registerUserEntity.getPhoneNumber());
        newUser.setPassword(registerUserEntity.getPassword());
        newUser.setStatus(USER_STATUS.ACTIVE);
        userRepository.save(newUser);

        // delete redis data
        registerUserRedisRepository.delete(registerUserEntity);
        log.info("[confirmRegisterOtp] - Successful OTP authentication for transactionId: {}", transactionId);
    }
}