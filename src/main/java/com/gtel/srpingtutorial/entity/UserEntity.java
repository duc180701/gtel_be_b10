package com.gtel.srpingtutorial.entity;

import com.gtel.srpingtutorial.entity.converters.UserStatusConverter;
import com.gtel.srpingtutorial.utils.USER_STATUS;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "users")
@Entity
public class UserEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Convert(converter = UserStatusConverter.class)
    private USER_STATUS status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public USER_STATUS getStatus() {
        return status;
    }

    public void setStatus(USER_STATUS status) {
        this.status = status;
    }
}
