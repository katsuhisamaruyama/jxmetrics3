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
 * Measures the value of Number of Variables.
 * 
 * @author Katsuhisa Maruyama
 */
public class LVAR extends Metric {
    
    public static final String Name = "LVAR";
    private static final String Description = "Number of Local Variables";
    
    public LVAR() {
        super(Name, Description);
    }
    
    public double calculate(JavaMethod jmethod) {
        return (double)(jmethod.getLocalVariables().size() + jmethod.getParameterSize());
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
