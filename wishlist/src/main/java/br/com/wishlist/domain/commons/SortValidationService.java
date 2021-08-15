/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.commons;

import br.com.wishlist.domain.commons.exception.InvalidSortParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author clayton.salgueiro
 */
public final class SortValidationService {

    private SortValidationService() {
    }

    /**
     * Validate types of sort parameters
     *
     * @param pageable The origin pageable
     * @param clazzEnum The properties type of sorter
     * @return The new Pageable with properties corrected
     */
    public static <E extends Enum<E>, S extends SortParameters<E>> Pageable validateSortTypes(final Pageable pageable,
            final Class<S> clazzEnum) {

        final List<S> enums = Arrays.asList(clazzEnum.getEnumConstants());
        final Function<String, Optional<S>> find = param -> enums.stream().filter(val -> val.equalsIgnoreCase(param))
                .findFirst();

        final List<Sort.Order> sorts = pageable.getSort().stream()
                .map(sort -> sort.withProperty(find.apply(sort.getProperty()).map(S::getParameter)
                .orElseThrow(() -> new InvalidSortParameterException("Os tipos permitidos de ordenação são: " + enums))))
                .collect(Collectors.toList());
        Sort identify = Sort.by(Sort.Order.by(enums.get(0).identify()));
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sorts).and(identify));

    }

}
