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
import org.jtool.srcmodel.JavaField;
import java.util.List;
import java.util.ArrayList;

/**
 * Measures the value of Lack of Cohesion Methods.
 * 
 * @author Katsuhisa Maruyama
 */
public class LCOM extends Metric {
    
    public static final String Name = "LCOM";
    private static final String Description = "Lack of Cohesion Methods";
    
    public LCOM() {
        super(Name, Description);
    }
    
    public double calculate(JavaClass jclass) {
        int accessedMethods = 0;
        int cohesiveMethods = 0;
        
        List<JavaMethod> jmethods = new ArrayList<JavaMethod>(jclass.getMethods());
        for (int i = 0; i < jmethods.size(); i++) {
            for (int j = i + 1; j < jmethods.size(); j++) {
                JavaMethod jm1 = jmethods.get(i);
                JavaMethod jm2 = jmethods.get(j);
                
                for (JavaField jf1 : jm1.getAccessedFieldsInProject()) {
                    for (JavaField jf2 : jm2.getAccessedFieldsInProject()) {
                        if (jf1.equals(jf2)) {
                            cohesiveMethods++;
                        } else {
                            accessedMethods++;
                        }
                    }
                }
            }
        }
        
        if (accessedMethods > cohesiveMethods) {
            return (double)(accessedMethods - cohesiveMethods);
        }
        return 0.0;
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
