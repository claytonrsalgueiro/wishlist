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
public class InvalidSortParameterException extends RuntimeException {

    private static final long serialVersionUID = -7311334985803688288L;

    private final String message;
}

