/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.srcmodel.JavaProject;

/**
 * Measures the value of Number of Packages.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOPG extends Metric {
    
    public static final String Name = "NOPG";
    private static final String Description = "Number of Packages";
    
    public NOPG() {
        super(Name, Description);
    }
    
    public double calculate(JavaProject jproject) {
        return (double)jproject.getPackages().size();
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(Name);
    }
}
