package com.gtel.srpingtutorial.redis.entities;

import com.gtel.srpingtutorial.model.response.AirportResponse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash("airport")
public class AirportRedisEntity implements Serializable {

    @Id
    private String code;

    private AirportResponse data;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private long timeToLive;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AirportResponse getData() {
        return data;
    }

    public void setData(AirportResponse data) {
        this.data = data;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }
}
