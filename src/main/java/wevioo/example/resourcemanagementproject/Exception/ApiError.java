package wevioo.example.resourcemanagementproject.Exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ApiError {

    private LocalDateTime timestamp;
    private boolean success;
    private int status;
    private String error;
    private String message;
    private String path;
    private String errorCode;
    private Map<String, String> errors;

    public ApiError(
            int status,
            String error,
            String message,
            String path
    ) {
        this.timestamp = LocalDateTime.now();
        this.success = false;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ApiError(
            int status,
            String error,
            String message,
            String path,
            Map<String, String> errors
    ) {
        this(status, error, message, path);
        this.errors = errors;
    }
}
