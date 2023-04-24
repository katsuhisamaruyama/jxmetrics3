/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.srcmodel.JavaMethod;

/**
 * Measures the value of Cyclomatic Complexity.
 * 
 * @author Katsuhisa Maruyama
 */
public class CYCLO extends Metric {
    
    public static final String Name = "CYCLO";
    private static final String Description = "Cyclomatic Complexity";
    
    public CYCLO() {
        super(Name, Description);
    }
    
    public double calculate(JavaMethod jmethod) {
        return MethodMetrics.getCyclomaticNumber(jmethod);
    }
    
    @Override
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(Name);
    }
    
    @Override
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MAX + Name);
    }
}
