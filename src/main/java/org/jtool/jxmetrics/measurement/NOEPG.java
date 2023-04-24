/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.srcmodel.JavaPackage;

/**
 * Measures the value of Number of Efferent Packages.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOEPG extends Metric {
    
    public static final String Name = "NOEPG";
    private static final String Description = "Number of Efferent Packages";
    
    public NOEPG() {
        super(Name, Description);
    }
    
    public double calculate(JavaPackage jpackage) {
        return (double)jpackage.getEfferentJavaPackages().size();
    }
    
    @Override
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(Name);
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(MAX + Name);
    }
}
