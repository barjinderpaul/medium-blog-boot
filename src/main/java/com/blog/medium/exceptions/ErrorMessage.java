package com.blog.medium.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ErrorMessage {
    private int errorCode;


    private String errorMessage;
    private String documentation;
    private HttpStatus httpStatus;

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public ErrorMessage(){

    }
    public ErrorMessage(int errorCode, String errorMessage, String documentation, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.documentation = documentation;
        this.httpStatus = httpStatus;
    }
    @Override
    public String toString() {
        return ""+errorMessage;
    }


}
