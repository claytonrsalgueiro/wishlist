/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author clayton.salgueiro
 */
@Getter
@AllArgsConstructor
public class NotAllowException extends RuntimeException {

    private static final long serialVersionUID = 1432998003355197399L;

    private final String message;

}
