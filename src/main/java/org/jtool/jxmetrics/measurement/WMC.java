/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.JavaMethod;

/**
 * Measures the value of Weighted Methods per Class.
 * 
 * @author Katsuhisa Maruyama
 */
public class WMC extends Metric {
    
    public static final String Name = "WMC";
    private static final String Description = "Weighted Methods per Class";
    
    public WMC() {
        super(Name, Description);
    }
    
    public double calculate(JavaClass jclass) {
        double wmc = 0;
        for (JavaMethod jmethod : jclass.getMethods()) {
            wmc = wmc + MethodMetrics.getCyclomaticNumber(jmethod);
        }
        return wmc;
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
