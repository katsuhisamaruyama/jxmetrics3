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
import org.jtool.srcmodel.JavaMethod;
import java.util.List;
import java.util.ArrayList;

/**
 * Measures the value of Response for Classes.
 * 
 * @author Katsuhisa Maruyama
 */
public class RFC extends Metric {
    
    public static final String Name = "RFC";
    private static final String Description = "Response for Classes";
    
    public RFC() {
        super(Name, Description);
    }
    
    public double calculate(JavaClass jclass) {
        List<JavaMethod> calledMethods = new ArrayList<JavaMethod>();
        for (JavaMethod jm : jclass.getMethods()) {
            for (JavaMethod m : jm.getCalledMethodsInProject()) {
                calledMethods.add(m);
            }
        }
        return (double)(jclass.getMethods().size() + calledMethods.size());
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
