/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.wishlist;

import br.com.wishlist.interfaces.dto.ProductDTO;
import br.com.wishlist.interfaces.dto.ProductWishlistDTO;
import br.com.wishlist.interfaces.dto.WishlistCustomerDTO;

/**
 *
 * @author clayton.salgueiro
 */
public interface WishlistCustomerService {

    /**
     * Add new product into wishlist
     *
     * @param productWishlistDTO
     * @return {@link WishlistCustomerDTO}
     */
    public WishlistCustomerDTO addProduct(final ProductWishlistDTO productWishlistDTO);

    /**
     * Remove Product of wishlist customer
     *
     * @param productWishlistDTO
     * @return {@link WishlistCustomerDTO}
     */
    public WishlistCustomerDTO removeProduct(final ProductWishlistDTO productWishlistDTO);

    /**
     * Show actual wishilist customer
     *
     * @param idCustomer
     * @return {@link WishlistCustomerDTO}
     */
    public WishlistCustomerDTO findByCustomerId(final String idCustomer);

    /**
     * Check existence product into customer wishlist
     *
     * @param idCustomer
     * @param idProduct
     * @return {@link ProductDTO}
     */
    public ProductDTO checkProductInWishlist(final String idCustomer, final String idProduct);

}
