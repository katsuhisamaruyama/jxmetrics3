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
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.JavaMethod;
import java.util.ArrayList;
import java.util.List;

/**
 * Measures the value of Tight Class Cohesion.
 * 
 * @author Katsuhisa Maruyama
 */
public class TCC extends Metric {
    
    public static final String Name = "TCC";
    private static final String Description = "Tight Class Cohesion";
    
    public TCC() {
        super(Name, Description);
    }
    
    public double calculate(JavaProject jproject, JavaClass jclass) {
        List<JavaMethod> methods = new ArrayList<JavaMethod>();
        for (JavaMethod jm : jclass.getMethods()) {
            if (!(jm.isAbstract() || jm.isConstructor())) {
                methods.add(jm);
            }
        }
        
        int n = methods.size();
        int np = (n * (n - 1)) / 2;
        int ndc = 0;
        
        List<List<String>> accessedFields = new ArrayList<List<String>>();
        for (JavaMethod jm : methods) {
            accessedFields.add(ClassMetrics.getAccessedFields(jproject, jclass, jm));
        }
        
        for (int i = 0; i < accessedFields.size(); i++) {
            for (int j = i + 1; j < accessedFields.size(); j++) {
                if (isConnected(accessedFields.get(i), accessedFields.get(j))) {
                    ndc++;
                }
            }
        }
        
        double result = 0;
        if (np > 0) {
            result = (double)ndc / np;
        }
        return result;
    }
    
    private boolean isConnected(List<String> method1, List<String> method2) {
        for (String field : method1) {
            if (method2.contains(field)) {
                return true;
            }
        }
        return false;
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
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(Name);
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(MAX + Name);
    }
    
    @Override
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(MAX + Name);
    }
    
    @Override
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MAX + Name);
    }
}
