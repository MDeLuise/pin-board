package com.github.mdeluise.pinboard.common;

import com.github.mdeluise.pinboard.query.QueryCriteria;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryHelper {
    public static <T, V extends Specification<T>> Optional<Specification<T>> andSpecification(List<V> criteria) {
        Iterator<V> itr = criteria.iterator();
        if (itr.hasNext()) {
            Specification<T> spec = Specification.where(itr.next());
            while (itr.hasNext()) {
                spec = spec.and(itr.next());
            }
            return Optional.of(spec);
        }
        return Optional.empty();
    }


    public static <V extends Sort> Optional<Sort> andSort(List<V> criteria) {
        Iterator<V> itr = criteria.iterator();
        if (itr.hasNext()) {
            Sort sort = itr.next();
            while (itr.hasNext()) {
                sort = sort.and(itr.next());
            }
            return Optional.of(sort);
        }
        return Optional.empty();
    }


    public static List<Sort> generateSortList(List<QueryCriteria> criteria) {
        return criteria.stream().map(criterion -> switch (criterion.getOperation()) {
            case SORT_ASC -> Sort.by(Sort.Order.asc(criterion.getKey()));
            case SORT_DESC -> Sort.by(Sort.Order.desc(criterion.getKey()));
            default -> null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
