/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.interfaces.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author clayton.salgueiro
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductWishlistDTO implements Serializable {

    private ProductDTO product;
    private CustomerDTO customer;
}
