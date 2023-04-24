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
 * Measures the value of Number of Efferent Fields.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOEFD extends Metric {
    
    public static final String Name = "NOEFD";
    private static final String Description = "Number of Efferent Fields";
    
    public NOEFD() {
        super(Name, Description);
    }
    
    public double calculate(JavaMethod jmethod) {
        return (double)jmethod.getAccessedFields().size();
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(Name);
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
