package wevioo.example.resourcemanagementproject.Exception.Custom;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }
}
