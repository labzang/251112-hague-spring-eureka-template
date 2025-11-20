package com.labzang.api.soccer.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Messenger {
    private String message;
    private Object data;
    private int status;
    
    public static Messenger success(String message, Object data) {
        return Messenger.builder()
                .message(message)
                .data(data)
                .status(200)
                .build();
    }
    
    public static Messenger success(Object data) {
        return success("success", data);
    }
    
    public static Messenger error(String message) {
        return Messenger.builder()
                .message(message)
                .data(null)
                .status(500)
                .build();
    }
}

