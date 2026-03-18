package by.beltam.practice.utility;

import org.springframework.data.domain.Sort;

import java.util.Set;

public class SortUtils {
    public static Sort getSortFrom(Set<String> availableValue,String requestedSort
            , String defaultField, String direction){
        String sortField = (requestedSort != null && availableValue.contains(requestedSort))
                ? requestedSort
                : defaultField;

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortField);
    }
}
