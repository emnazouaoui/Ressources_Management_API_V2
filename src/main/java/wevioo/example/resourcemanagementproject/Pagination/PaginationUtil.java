package wevioo.example.resourcemanagementproject.Pagination;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

@Component
public class PaginationUtil {

    public Sort sortingCriteria(
            CustomSort sort,
            Sort.Direction defaultDirection,
            String defaultColumn) {

        if (sort != null) {
            if (sort.getOrder() == null) {
                throw new RuntimeException("sort.order is required (ASC or DESC)");
            }
            if (ObjectUtils.isEmpty(sort.getColumnKey())) {
                throw new RuntimeException("sort.columnKey is required");
            }
        }

        return sort == null
                ? Sort.by(defaultDirection, defaultColumn)
                : Sort.by(sort.getOrder(), sort.getColumnKey().split(","));
    }

    public Pageable createPageable(Integer page, Integer pageSize, Sort sort) {
        if (page == null || pageSize == null) {
            throw new RuntimeException("page and pageSize are required");
        }
        if (page < 1 || pageSize < 1) {
            throw new RuntimeException("page and pageSize must be >= 1");
        }
        return sort != null
                ? PageRequest.of(page - 1, pageSize, sort)
                : PageRequest.of(page - 1, pageSize);
    }
}
