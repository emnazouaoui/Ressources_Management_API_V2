package wevioo.example.resourcemanagementproject.Pagination;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedResponse<T> {

    private List<T> content;        // les données
    private int page;               // numéro de page actuelle (commence à 1)
    private int pageSize;           // taille de page
    private long totalElement;      // total des éléments
    private int totalPage;          // total des pages
}
