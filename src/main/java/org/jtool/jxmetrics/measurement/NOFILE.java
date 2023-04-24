/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.srcmodel.JavaProject;

/**
 * Measures the value of Number of Files.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOFILE extends Metric {
    
    public static final String Name = "NOFILE";
    private static final String Description = "Number of Files";
    
    public NOFILE() {
        super(Name, Description);
    }
    
    public double calculate(JavaProject jproject) {
        return (double)jproject.getFiles().size();
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(Name);
    }
}
