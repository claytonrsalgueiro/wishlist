/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.product;

import br.com.wishlist.interfaces.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author clayton.salgueiro
 */
@Service
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Page<ProductDTO> findBySearch(final String search, final Pageable pageable) {
        final String searchField = Optional.ofNullable(search).map(sea -> sea).orElse("");
        Page<Product> resultPage = this.productRepository.findByNameContainingIgnoreCase(searchField, pageable);
        List<ProductDTO> resultList = resultPage.stream().map(prod -> this.objectMapper.convertValue(prod, ProductDTO.class)).collect(Collectors.toList());

        return new PageImpl<>(resultList, pageable, resultPage.getTotalElements());
    }

    @Override
    public ProductDTO create(final ProductDTO productDTO) {
        final Product product = Product.of(productDTO.getAmount(), productDTO.getName(), productDTO.getDescription());
        final Product productSaved = this.productRepository.save(product);

        return this.objectMapper.convertValue(productSaved, ProductDTO.class);
    }

}
