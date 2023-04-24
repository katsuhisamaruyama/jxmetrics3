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
 * An interface for metric measurements.
 * 
 * @author Katsuhisa Maruyama
 */
public interface IMetric {
    
    public String getName();
    public String getDescription();
    
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException;
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException;
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException;
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException;
    public double valueOf(FieldMetrics mfield) throws UnsupportedMetricsException;
    
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException;
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException;
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException;
    public double maxValueIn(MethodMetrics mmethod) throws UnsupportedMetricsException;
    public double maxValueIn(FieldMetrics mfield) throws UnsupportedMetricsException;
}