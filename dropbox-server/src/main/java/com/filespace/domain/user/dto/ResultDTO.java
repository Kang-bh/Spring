package com.filespace.domain.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class ResultDTO<T> {
    private String status;
    private String message;
    private T data;


    public ResultDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResultDTO(String status, T data) {
        this.status = status;
        this.data = data;
    }

    public ResultDTO(String status,String message, T data) {
        this.status = status;
        this.data = data;
        this.message = message;

    }

    // getter 및 setter 생략 (생략 가능)
    @Override
    public String toString() {
        return "ResponseDTO{" + "\n" +
                "status='" + status + '\'' + "\n" +
                ", message='" + message + '\'' + "\n" +
                ", data=" + data + "\n" +
                '}';
    }
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
            return null;
        }
    }
}
