package com.marinho.bankslips.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private List<Error> errors;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public void addError(String field, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }

        errors.add(Error.builder()
                .field(field)
                .message(message)
                .build());
    }

    @Data
    @Builder
    static class Error {
        private String field;
        private String message;
    }
}
