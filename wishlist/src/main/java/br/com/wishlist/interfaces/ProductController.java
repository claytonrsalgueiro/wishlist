/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.interfaces;

import br.com.wishlist.application.ProductFacade;
import br.com.wishlist.config.security.RoleAuthUtils;
import br.com.wishlist.domain.commons.ApiPageable;
import br.com.wishlist.domain.commons.ProductSortEnum;
import br.com.wishlist.domain.commons.SortValidationService;
import br.com.wishlist.interfaces.dto.ProductDTO;
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
@RequestMapping("api/v1/products")
@Api(tags = {"Products"})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@RestController
public class ProductController {

    private final ProductFacade productFacade;

    @ApiPageable
    @GetMapping
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<Page<ProductDTO>> findBySearch(@RequestParam(name = "search", required = false) final String search,
            @PageableDefault(sort = "NAME", direction = Sort.Direction.ASC, page = 0, size = 10) final Pageable pageable) {

        final Pageable validatedPageable = SortValidationService.validateSortTypes(pageable,
                ProductSortEnum.class);

        return Optional
                .ofNullable(this.productFacade.findBySearch(search, validatedPageable))
                .filter(Page::hasContent).map(ResponseEntity::ok).orElseGet(ResponseEntity.noContent()::build);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<ProductDTO> create(@RequestBody final ProductDTO productDTO) {

        final ProductDTO wishSaved = this.productFacade.create(productDTO);

        URI uri = UriComponentsBuilder.fromUriString("api/v1/products/{id}").buildAndExpand(wishSaved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(wishSaved);
    }

}
