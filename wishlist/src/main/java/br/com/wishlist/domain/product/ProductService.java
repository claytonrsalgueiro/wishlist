/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.product;

import br.com.wishlist.interfaces.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author clayton.salgueiro
 */
public interface ProductService {

    public Page<ProductDTO> findBySearch(final String search, final Pageable pageable);

    public ProductDTO create(final ProductDTO productDTO);
    
}
