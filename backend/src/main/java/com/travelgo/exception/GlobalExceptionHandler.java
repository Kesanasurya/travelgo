package com.travelgo.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<?> notFound(Exception e,HttpServletRequest r){return error(HttpStatus.NOT_FOUND,e.getMessage(),r);}
 @ExceptionHandler({IllegalArgumentException.class,MethodArgumentNotValidException.class}) ResponseEntity<?> bad(Exception e,HttpServletRequest r){String m=e instanceof MethodArgumentNotValidException v?v.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining(", ")):e.getMessage();return error(HttpStatus.BAD_REQUEST,m,r);}
 @ExceptionHandler(AuthenticationException.class) ResponseEntity<?> unauthorized(AuthenticationException e,HttpServletRequest r){return error(HttpStatus.UNAUTHORIZED,"Invalid email or password",r);}
 @ExceptionHandler(Exception.class) ResponseEntity<?> server(Exception e,HttpServletRequest r){return error(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error",r);}
 private ResponseEntity<?> error(HttpStatus s,String m,HttpServletRequest r){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),m,r.getRequestURI()));}
 record ApiError(Instant timestamp,int status,String message,String path){}
}
