package com.hasandag.banking.loanapi.responsemodel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
public class LoanApiResponse<T> implements Serializable {
    private HttpStatus status;
    private transient T data;
    private LoanApiResponseMessage message;

    public LoanApiResponse() {
    }

    public LoanApiResponse(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
    }

    public LoanApiResponse(LoanApiResponseMessage message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
