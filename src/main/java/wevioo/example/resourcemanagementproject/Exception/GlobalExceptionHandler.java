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

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================= VALIDATION =================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return new ApiError(
                400,
                "Validation Error",
                "Invalid request data",
                request.getRequestURI(),
                fieldErrors
        );
    }

    // ================= NOT FOUND =================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiError handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        return new ApiError(
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ================= BAD REQUEST =================

    @ExceptionHandler(BadRequestException.class)
    public ApiError handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ) {

        return new ApiError(
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ================= CONFLICT =================

    @ExceptionHandler(ConflictException.class)
    public ApiError handleConflict(
            ConflictException ex,
            HttpServletRequest request
    ) {

        return new ApiError(
                409,
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ================= UNAUTHORIZED =================

    @ExceptionHandler(UnauthorizedException.class)
    public ApiError handleUnauthorized(
            UnauthorizedException ex,
            HttpServletRequest request
    ) {

        return new ApiError(
                401,
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ================= GENERIC =================

    @ExceptionHandler(Exception.class)
    public ApiError handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        return new ApiError(
                500,
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
    }
}