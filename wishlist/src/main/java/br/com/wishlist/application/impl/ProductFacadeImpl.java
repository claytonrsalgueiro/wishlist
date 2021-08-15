/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.application.impl;

import br.com.wishlist.application.ProductFacade;
import br.com.wishlist.domain.product.ProductService;
import br.com.wishlist.interfaces.dto.ProductDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 *
 * @author clayton.salgueiro
 */
@Component
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductFacadeImpl implements ProductFacade {

    private final ProductService productService;

    @Override
    public ProductDTO create(final ProductDTO productDTO) {
        return this.productService.create(productDTO);
    }

    @Override
    public Page<ProductDTO> findBySearch(final String search, final Pageable pageable) {
        return this.productService.findBySearch(search, pageable);
    }
}
