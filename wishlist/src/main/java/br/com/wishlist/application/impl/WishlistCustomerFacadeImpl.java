/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.application.impl;

import br.com.wishlist.application.WishlistCustomerFacade;
import br.com.wishlist.domain.wishlist.WishlistCustomerService;
import br.com.wishlist.interfaces.dto.ProductDTO;
import br.com.wishlist.interfaces.dto.ProductWishlistDTO;
import br.com.wishlist.interfaces.dto.WishlistCustomerDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * @author clayton.salgueiro
 */
@Component
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class WishlistCustomerFacadeImpl implements WishlistCustomerFacade {

    private final WishlistCustomerService wishlistCustomerService;

    @Override
    public WishlistCustomerDTO addProduct(final ProductWishlistDTO productWishlistDTO) {
        return this.wishlistCustomerService.addProduct(productWishlistDTO);
    }

    @Override
    public WishlistCustomerDTO removeProduct(final ProductWishlistDTO productWishlistDTO) {
        return this.wishlistCustomerService.removeProduct(productWishlistDTO);
    }

    @Override
    public WishlistCustomerDTO findByCustomerId(final String idCustomer) {
        return this.wishlistCustomerService.findByCustomerId(idCustomer);
    }

    @Override
    public ProductDTO checkProductInWishlist(final String idCustomer, final String idProduct) {
        return this.wishlistCustomerService.checkProductInWishlist(idCustomer, idProduct);
    }

}
