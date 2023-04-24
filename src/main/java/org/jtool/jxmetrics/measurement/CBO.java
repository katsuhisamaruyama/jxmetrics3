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
import java.util.List;
import java.util.ArrayList;

/**
 * Measures the value of Coupling Between Objects.
 * 
 * @author Katsuhisa Maruyama
 */
public class CBO extends Metric {
    
    public static final String Name = "CBO";
    private static final String Description = "Coupling Between Objects";
    
    /**
     * Creates an object returning a metric measurement.
     */
    public CBO() {
        super(Name, Description);
    }
    
    public double calculate(JavaClass jclass) {
        List<JavaClass> classes = new ArrayList<JavaClass>();
        collectCoupledClasses(jclass, classes);
        return (double)classes.size();
    }
    
    private void collectCoupledClasses(JavaClass jclass, List<JavaClass> classes) {
        for (JavaClass jc : jclass.getAfferentClassesInProject()) {
            if (!classes.contains(jc)) {
                classes.add(jc);
                collectCoupledClasses(jc, classes);
            }
        }
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
