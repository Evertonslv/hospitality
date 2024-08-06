package com.hospitality.api.infraestructure.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HospitalityResponse<T> implements Serializable {
    private HttpStatus status;
    private String code;
    private String message;
    private T data;

    public HospitalityResponse(HttpStatus status, String message) {
        this.status = status;
        this.code = String.valueOf(status.value());
        this.message = message;
    }

    public HospitalityResponse(HttpStatus status, T data) {
        this.status = status;
        this.code = String.valueOf(status.value());
        this.data = data;
    }

}
