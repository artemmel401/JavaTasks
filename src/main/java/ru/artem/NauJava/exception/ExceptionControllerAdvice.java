package ru.artem.NauJava.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.artem.NauJava.dto.auth.LoginErrorResponse;

@ControllerAdvice
public class ExceptionControllerAdvice
{
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public LoginErrorResponse authenticationFailed(AuthenticationException e) {
        return new LoginErrorResponse("Invalid email or password");
    }

    @ExceptionHandler(java.lang.Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Exception exception(java.lang.Exception e)
    {
        return Exception.create(e);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception exception(ResourceNotFoundException e)
    {
        return Exception.create(e);
    }
}
