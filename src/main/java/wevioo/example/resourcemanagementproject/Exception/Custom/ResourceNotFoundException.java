package wevioo.example.resourcemanagementproject.Exception.Custom;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
