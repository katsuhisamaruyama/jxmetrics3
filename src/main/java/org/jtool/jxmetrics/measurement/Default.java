/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.FieldMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;

/**
 * Measures nothing and returns always default values.
 * 
 * @author Katsuhisa Maruyama
 */
public class Default extends Metric {
    
    public static final String Name = "(default)";
    private static final String Description = "Default metric";
    
    public Default() {
        super(Name, Description);
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double valueOf(FieldMetrics mfield) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException {
        return -1.0;
    }
    
    @Override
    public double maxValueIn(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return -1;
    }
    
    @Override
    public double maxValueIn(FieldMetrics mfield) throws UnsupportedMetricsException {
        return -1.0;
    }
}