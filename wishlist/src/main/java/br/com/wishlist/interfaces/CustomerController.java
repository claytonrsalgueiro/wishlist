/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.interfaces;

import br.com.wishlist.application.CustomerFacade;
import br.com.wishlist.config.security.RoleAuthUtils;
import br.com.wishlist.domain.commons.ApiPageable;
import br.com.wishlist.domain.commons.ClientSortEnum;
import br.com.wishlist.domain.commons.SortValidationService;
import br.com.wishlist.interfaces.dto.CustomerDTO;
import io.swagger.annotations.Api;
import java.net.URI;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author clayton.salgueiro
 */
@RequestMapping("api/v1/customers")
@Api(tags = {"Customers"})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@RestController
public class CustomerController {

    private final CustomerFacade customerFacade;

    @ApiPageable
    @GetMapping
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<Page<CustomerDTO>> findBySearch(@RequestParam(name = "search", required = false) final String search,
            @PageableDefault(sort = "NAME", direction = Sort.Direction.ASC, page = 0, size = 5) final Pageable pageable) {

        final Pageable validatedPageable = SortValidationService.validateSortTypes(pageable,
                ClientSortEnum.class);

        return Optional
                .ofNullable(this.customerFacade.findBySearch(search, validatedPageable))
                .filter(Page::hasContent).map(ResponseEntity::ok).orElseGet(ResponseEntity.noContent()::build);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<CustomerDTO> create(@RequestBody final CustomerDTO customerDTO) {

        final CustomerDTO wishSaved = this.customerFacade.create(customerDTO);

        URI uri = UriComponentsBuilder.fromUriString("api/v1/customers/{id}").buildAndExpand(wishSaved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(wishSaved);
    }
}
