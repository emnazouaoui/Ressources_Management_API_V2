package wevioo.example.resourcemanagementproject.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wevioo.example.resourcemanagementproject.Exception.Custom.BadRequestException;
import wevioo.example.resourcemanagementproject.Exception.Custom.ConflictException;
import wevioo.example.resourcemanagementproject.Exception.Custom.ResourceNotFoundException;
import wevioo.example.resourcemanagementproject.Exception.Custom.UnauthorizedException;
import wevioo.example.resourcemanagementproject.Exception.Custom.ValidationException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================= VALIDATION (annotations @Valid) =================
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> fieldErrors.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return new ApiError(
                400,
                "Validation Error",
                "Invalid request data",
                request.getRequestURI(), fieldErrors);
    }

    // ================= VALIDATION (Validator classes) =================
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiError handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {

        return new ApiError(
                400,
                "Validation Error",
                "Invalid request data",
                request.getRequestURI(),
                ex.getFieldErrors());   // ← Map<String, String> des erreurs par field
    }

    // ================= NOT FOUND =================
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiError handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return new ApiError(
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI());
    }

    // ================= BAD REQUEST =================
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiError handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {

        return new ApiError(
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI());
    }

    // ================= CONFLICT =================
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ApiError handleConflict(
            ConflictException ex,
            HttpServletRequest request) {

        return new ApiError(
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI());
    }

    // ================= UNAUTHORIZED =================
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ApiError handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request) {

        return new ApiError(
                401,
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI());
    }

    // ================= GENERIC =================
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiError handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        return new ApiError(
                500,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI());
    }

}