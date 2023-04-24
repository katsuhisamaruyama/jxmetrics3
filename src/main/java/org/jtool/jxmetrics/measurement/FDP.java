/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaMethod;
import java.util.Set;

/**
 * Measures the value of Foreign Data Providers.
 * 
 * @author Katsuhisa Maruyama
 */
public class FDP extends Metric {
    
    public static final String Name = "FDP";
    private static final String Description = "Foreign Data Providers";
    
    public FDP() {
        super(Name, Description);
    }
    
    public double calculate(JavaProject jproject, JavaMethod jmethod) {
        Set<String> accessedClasses = MethodMetrics.getAccessedClasses(jproject, jmethod);
        return (double)accessedClasses.size();
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
