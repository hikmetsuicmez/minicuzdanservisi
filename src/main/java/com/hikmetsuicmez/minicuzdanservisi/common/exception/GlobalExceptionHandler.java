package com.hikmetsuicmez.minicuzdanservisi.common.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.hikmetsuicmez.minicuzdanservisi.account.exception.AccountNotFoundException;
import com.hikmetsuicmez.minicuzdanservisi.account.exception.InvalidAccountStateException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// AccountNotFoundException -> 404 NOT_FOUND
	@ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFoundException(AccountNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, 
                ex.getMessage()
        );
        problemDetail.setTitle("Hesap Bulunamadı");
        return problemDetail;
    }
	
	// Validation Hataları (MethodArgumentNotValidException) -> 400 BAD_REQUEST
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, 
                "Bir veya daha fazla alanda doğrulama hatası oluştu"
        );
        problemDetail.setTitle("Geçersiz İstek İçeriği");

        Map<String, List<String>> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
        	errors.computeIfAbsent(error.getField(), k -> new ArrayList<>())
            .add(error.getDefaultMessage());
        }
        
        problemDetail.setProperty("invalidFields", errors);

        return problemDetail;
    }
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
		String paramName = ex.getName();
	    Object invalidValue = ex.getValue();
	    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "bilinmeyen tip";

	    String detailMessage = String.format("'%s' parametresi için gönderilen '%s' değeri geçerli bir %s tipi değil.", 
	            paramName, invalidValue, requiredType);

	    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
	            HttpStatus.BAD_REQUEST, 
	            detailMessage
	    );
	    problemDetail.setTitle("Geçersiz Parametre Tipi");

	    return problemDetail;
	}
	
	// Hesap Durumu / Tipi İş Kuralı Hataları -> 422 UNPROCESSABLE_CONTENT
    @ExceptionHandler(InvalidAccountStateException.class)
    public ProblemDetail handleInvalidAccountStateException(InvalidAccountStateException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT, 
                ex.getMessage()
        );
        problemDetail.setTitle("Geçersiz Hesap Durumu");
        return problemDetail;
    }
}
