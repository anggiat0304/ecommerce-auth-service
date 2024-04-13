package com.ecommerce.authservice.dto.response;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GenericResponse<T> {
    private String message;
    private boolean success;
    private T Data;
    public GenericResponse(){

    }

    public GenericResponse(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        Data = data;
    }

    public static <T> GenericResponse<T> success(String message, T data) {
        return new GenericResponse<>(message, true, data);
    }

    public static <T> GenericResponse<T> error(String message) {
        return new GenericResponse<>(message, false, null);
    }
}