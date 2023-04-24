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
 * Measures the value of Number of Afferent Packages.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOAPG extends Metric {
    
    public static final String Name = "NOAPG";
    private static final String Description = "Number of Afferent Packages";
    
    public NOAPG() {
        super(Name, Description);
    }
    
    public double calculate(JavaPackage jpackage) {
        return (double)jpackage.getAfferentJavaPackages().size();
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
