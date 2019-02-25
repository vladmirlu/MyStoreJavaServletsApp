package com.selling.store.exception;

import org.apache.log4j.Logger;

/**
 * Exception for handling cases when impossible to get item data
 * */
public class ItemNotFoundException extends RuntimeException {

    /**
     * Builds the exception and logs the reason
     * */
    public ItemNotFoundException(String reason){
        super(reason);
        Logger.getLogger(ItemNotFoundException.class).error(reason);
    }
}
