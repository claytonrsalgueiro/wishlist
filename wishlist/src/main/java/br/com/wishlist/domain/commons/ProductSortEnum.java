/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author clayton.salgueiro
 */
@Getter
@AllArgsConstructor
public enum ProductSortEnum implements SortParameters<ProductSortEnum> {

    NAME("name"),
    DESCRIPTION("description");

    private String parameter;

}