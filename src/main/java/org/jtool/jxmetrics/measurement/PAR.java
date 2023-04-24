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
 * Measures the value of Number of Parameters.
 * 
 * @author Katsuhisa Maruyama
 */
public class PAR extends Metric {
    
    public static final String Name = "PAR";
    private static final String Description = "Number of Parameters";
    
    public PAR() {
        super(Name, Description);
    }
    
    public double calculate(JavaMethod jmethod) {
        return (double)jmethod.getParameterSize();
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
