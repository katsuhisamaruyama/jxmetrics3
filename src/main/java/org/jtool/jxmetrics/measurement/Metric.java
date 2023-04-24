/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.FieldMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;

/**
 * A default implementation for metric measurements.
 * 
 * @author Katsuhisa Maruyama
 */
public class Metric implements IMetric {
    
    private String name = "";
    private String description = "";
    
    public static final String MAX = "MAX_";
    
    public Metric() {
    }
    
    protected Metric(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for a project " + mproject.getName());
    }
    
    @Override
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for package " + mpackage.getName());
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for class " + mclass.getQualifiedName());
    }
    
    @Override
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for method " + mmethod.getQualifiedName());
    }
    
    @Override
    public double valueOf(FieldMetrics mfield) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for field " + mfield.getQualifiedName());
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for project " + mproject.getName());
    }
    
    @Override
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for package " + mpackage.getName());
    }
    
    @Override
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for class " + mclass.getQualifiedName());
    }
    
    @Override
    public double maxValueIn(MethodMetrics mmethod) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for method " + mmethod.getQualifiedName());
    }
    
    @Override
    public double maxValueIn(FieldMetrics mfield) throws UnsupportedMetricsException {
        throw new UnsupportedMetricsException(this.getDescription() + " for field " + mfield.getQualifiedName());
    }
}