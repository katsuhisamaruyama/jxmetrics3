/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.JavaField;

/**
 * Measures the value of Number of Public Fields.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOPFD extends Metric {
    
    public static final String Name = "NOPFD";
    private static final String Description = "Number of Public Fields";
    
    public NOPFD() {
        super(Name, Description);
    }
    
    public double calculate(JavaClass jclass) {
        int num = 0;
        for (JavaField jf : jclass.getFields()) {
            if (jf.isPublic()) {
                num++;
            }
        }
        return (double)num;
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(Name);
    }
    
    @Override
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(Name);
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(Name);
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(MAX + Name);
    }
    
    @Override
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(MAX + Name);
    }
}
