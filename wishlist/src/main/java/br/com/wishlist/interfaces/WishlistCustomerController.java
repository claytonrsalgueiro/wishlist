/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.interfaces;

import br.com.wishlist.application.WishlistCustomerFacade;
import br.com.wishlist.config.security.RoleAuthUtils;
import br.com.wishlist.interfaces.dto.CustomerDTO;
import br.com.wishlist.interfaces.dto.ProductDTO;
import br.com.wishlist.interfaces.dto.ProductWishlistDTO;
import br.com.wishlist.interfaces.dto.WishlistCustomerDTO;
import io.swagger.annotations.Api;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author clayton.salgueiro
 */
@RequestMapping("api/v1/wishlist")
@Api(tags = {"Wishlist Customer"})
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@RestController
public class WishlistCustomerController {

    private final WishlistCustomerFacade wishlistCustomerFacade;

    /**
     * Add new product into wishlist
     *
     * @param productWishlistDTO
     * @return {@link ResponseEntity<WishlistCustomerDTO>}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<WishlistCustomerDTO> addProduct(@RequestBody final ProductWishlistDTO productWishlistDTO) {

        final WishlistCustomerDTO wishSaved = this.wishlistCustomerFacade.addProduct(productWishlistDTO);

        return ResponseEntity.ok(wishSaved);
    }

    /**
     * Remove Product of wishlist customer
     *
     * @param idProduct
     * @param idCustomer
     * @return {@link ResponseEntity<WishlistCustomerDTO>}
     */
    @DeleteMapping("/{idProduct}/{idCustomer}")
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<WishlistCustomerDTO> removeProduct(@PathVariable final String idProduct, @PathVariable final String idCustomer) {

        final WishlistCustomerDTO wishSaved = this.wishlistCustomerFacade.removeProduct(ProductWishlistDTO.builder()
                .product(ProductDTO.builder()
                        .id(idProduct)
                        .build())
                .customer(CustomerDTO.builder()
                        .id(idCustomer)
                        .build())
                .build());

        return ResponseEntity.ok(wishSaved);
    }

    /**
     * Show actual wishilist customer
     *
     * @param idCustomer
     * @return {@link ResponseEntity<WishlistCustomerDTO>}
     */
    @GetMapping
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<WishlistCustomerDTO> findByCustomerId(@RequestParam final String idCustomer) {

        return Optional
                .ofNullable(this.wishlistCustomerFacade.findByCustomerId(idCustomer))
                .map(ResponseEntity::ok).orElseGet(ResponseEntity.noContent()::build);
    }

    /**
     * Check existence product into customer wishlist
     *
     * @param idCustomer
     * @param idProduct
     * @return {@link ResponseEntity<ProductDTO:}
     */
    @GetMapping("/check-product")
    @PreAuthorize("hasAnyRole(" + RoleAuthUtils.ADMIN + ")")
    public ResponseEntity<ProductDTO> checkProductInWishlist(@RequestParam final String idCustomer, @RequestParam final String idProduct) {

        return Optional
                .ofNullable(this.wishlistCustomerFacade.checkProductInWishlist(idCustomer, idProduct))
                .map(ResponseEntity::ok).orElseGet(ResponseEntity.noContent()::build);
    }

}
