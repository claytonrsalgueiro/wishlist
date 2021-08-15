/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.wishlist.domain.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreConditionException extends RuntimeException {
	private static final long serialVersionUID = 3480029103525226364L;
	private final String message;
}