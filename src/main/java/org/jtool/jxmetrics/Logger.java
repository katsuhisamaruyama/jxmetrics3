/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics;

/**
 * Print logs.
 * 
 * @author Katsuhisa Maruyama
 */
public class Logger {
    
    private static Logger instance = new Logger();
    
    private boolean visible = true;
    
    private Logger() {
    }
    
    public static Logger getInstance() {
        return instance;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void printLog(String mesg) {
        if (visible) {
            System.out.println(mesg);
            System.out.flush();
        }
    }
}
