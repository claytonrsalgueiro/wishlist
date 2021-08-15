/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.commons;

/**
 *
 * @author clayton.salgueiro
 */
@FunctionalInterface
public interface SortParameters<T extends Enum<T>> {

    public String getParameter();

    public default String identify() {
        return "id";
    }

    public default boolean equalsIgnoreCase(final String param) {
        return this.toString().equalsIgnoreCase(param);
    }
}
