package wevioo.example.resourcemanagementproject.Pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class CustomSort {

    private String columnKey;       // ex: "name" ou "name,email"
    private Sort.Direction order;   // ASC ou DESC
}
