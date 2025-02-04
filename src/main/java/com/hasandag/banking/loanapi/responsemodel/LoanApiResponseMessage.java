package com.hasandag.banking.loanapi.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanApiResponseMessage implements Serializable {
    private String title;
    private String details;
    private String exception;

    public LoanApiResponseMessage(String title) {
        this.title = title;
    }

    public LoanApiResponseMessage(String title, String details) {
        this.title = title;
        this.details = details;
    }

}
