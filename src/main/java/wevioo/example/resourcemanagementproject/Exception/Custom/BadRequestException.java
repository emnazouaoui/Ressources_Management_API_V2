package wevioo.example.resourcemanagementproject.Exception.Custom;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}
