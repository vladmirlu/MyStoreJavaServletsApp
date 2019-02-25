package com.selling.store.exception;

import org.apache.log4j.Logger;

/**
 * Exception for handling application server side error
 * */
public class ApplicationErrorException extends RuntimeException {

    /**
     * Builds the exception and logs the reason
     * */
   public ApplicationErrorException(String message){
        super(message);
        Logger.getLogger(ItemNotFoundException.class).error(message);
    }
}
