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
public enum ErrorMessageEnum {

	DUPLICATED("ERROR: duplicate key value violates unique constraint",
			"Duplicate values ​​have been entered relative to another record in unique fields (Generic error)."),
	DELETE("ERROR: update or delete on table", "Unable to delete record that is in another record (Generic error).");
	private String error;
	private String message;

}