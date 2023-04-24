/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

/**
 * An object encapsulating a exception with respect to a metric measurement.
 * 
 * @author Katsuhisa Maruyama
 */
public class UnsupportedMetricsException extends Exception {
    
    private static final long serialVersionUID = -1L;
    
    public UnsupportedMetricsException() {
        super();
    }
    
    public UnsupportedMetricsException(String mesg) {
        super("Unsupported: " + mesg);
    }
    
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
