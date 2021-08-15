/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.interfaces.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author clayton.salgueiro
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO implements Serializable {

    @Setter
    private String id;
    private BigDecimal amount;
    @Setter
    private String name;
    private String description;

}
