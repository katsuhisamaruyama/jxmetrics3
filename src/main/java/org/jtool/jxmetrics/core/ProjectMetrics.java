/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxmetrics.measurement.LOC;
import org.jtool.jxmetrics.measurement.NOST;
import org.jtool.jxmetrics.measurement.ATFD;
import org.jtool.jxmetrics.measurement.CBO;
import org.jtool.jxmetrics.measurement.DIT;
import org.jtool.jxmetrics.measurement.NOC;
import org.jtool.jxmetrics.measurement.RFC;
import org.jtool.jxmetrics.measurement.TCC;
import org.jtool.jxmetrics.measurement.WMC;
import org.jtool.jxmetrics.measurement.LCOM;
import org.jtool.jxmetrics.measurement.NOACL;
import org.jtool.jxmetrics.measurement.NOECL;
import org.jtool.jxmetrics.measurement.NOCL;
import org.jtool.jxmetrics.measurement.NOMD;
import org.jtool.jxmetrics.measurement.NOFD;
import org.jtool.jxmetrics.measurement.NOPMD;
import org.jtool.jxmetrics.measurement.NOPFD;
import org.jtool.jxmetrics.measurement.NOMDFD;
import org.jtool.jxmetrics.measurement.NOFILE;
import org.jtool.jxmetrics.measurement.NOPG;
import org.jtool.jxmetrics.measurement.Metric;
import org.jtool.srcmodel.JavaProject;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stores metric information on a project.
 * 
 * @author Katsuhisa Maruyama
 */
public class ProjectMetrics extends Metrics {
    
    public static final String Id = "ProjectMetrics";
    
    private JavaProject jproject = null;
    
    protected String path;
    
    protected List<PackageMetrics> packages = new ArrayList<>();
    
    public ProjectMetrics(String name, String path) {
        super(name);
        this.path = path;
    }
    
    public ProjectMetrics(JavaProject jproject) {
        this(jproject.getName(), jproject.getTopPath());
        this.jproject = jproject;
    }
    
    public void collect(JavaProject jproject) {
        PackageMetrics.sort(packages);
        collectMetrics(jproject);
        collectMetricsMax();
    }
    
    public String getName() {
        return fqn;
    }
    
    public String getPath() {
        return path;
    }
    
    public JavaProject getJavaProject() {
        return jproject;
    }
    
    public void addPackage(PackageMetrics mpackage) {
        if (!packages.contains(mpackage)) {
            packages.add(mpackage);
        }
    }
    
    public List<PackageMetrics> getPackages() {
        return packages;
    }
    
    public void sortPackages() {
        PackageMetrics.sort(packages);
    }
    
    public List<ClassMetrics> getClasses() {
        List<ClassMetrics> classes = new ArrayList<ClassMetrics>();
        for (PackageMetrics mpackage : getPackages()) {
            classes.addAll(mpackage.getClasses());
        }
        ClassMetrics.sort(classes);
        return classes;
    }
    
    private void collectMetrics(JavaProject jproject) {
        putSumMetricValue(LOC.Name);
        putSumMetricValue(NOST.Name);
        
        putMetricValue(NOFILE.Name, new NOFILE().calculate(jproject));
        putMetricValue(NOPG.Name, new NOPG().calculate(jproject));
        
        putSumMetricValue(NOCL.Name);
        putSumMetricValue(NOMD.Name);
        putSumMetricValue(NOFD.Name);
        putSumMetricValue(NOMDFD.Name);
        putSumMetricValue(NOPMD.Name);
        putSumMetricValue(NOPFD.Name);
        
        putSumMetricValue(NOACL.Name);
        putSumMetricValue(NOECL.Name);
        
        putSumMetricValue(CBO.Name);
        putSumMetricValue(DIT.Name);
        putSumMetricValue(NOC.Name);
        putSumMetricValue(RFC.Name);
        putSumMetricValue(WMC.Name);
        putSumMetricValue(LCOM.Name);
    }
    
    protected void collectMetricsMax() {
        putMaxMetricValue(LOC.Name);
        putMaxMetricValue(NOST.Name);
        
        putMaxMetricValue(NOCL.Name);
        putMaxMetricValue(NOMD.Name);
        putMaxMetricValue(NOFD.Name);
        putMaxMetricValue(NOMDFD.Name);
        putMaxMetricValue(NOPMD.Name);
        putMaxMetricValue(NOPFD.Name);
        
        putMaxMetricValue(NOACL.Name);
        putMaxMetricValue(NOECL.Name);
        
        putMaxMetricValue(CBO.Name);
        putMaxMetricValue(DIT.Name);
        putMaxMetricValue(NOC.Name);
        putMaxMetricValue(RFC.Name);
        putMaxMetricValue(WMC.Name);
        putMaxMetricValue(LCOM.Name);
        
        putMaxMetricValue(ATFD.Name);
        putMaxMetricValue(TCC.Name);
    }
    
    private void putSumMetricValue(String sort) {
        metricValues.put(sort, sum(sort));
    }
    
    private void putMaxMetricValue(String sort) {
        metricValues.put(Metric.MAX + sort, max(sort));
    }
    
    private Double sum(String sort) {
        double value = 0;
        for (PackageMetrics mpackage : packages) {
            value = value + mpackage.getMetricValue(sort);
        }
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    protected Double max(String sort) {
        double value = 0;
        for (PackageMetrics mpackage : packages) {
            for (ClassMetrics mclass : mpackage.getClasses()) {
                value = Math.max(value, mclass.getMetricValue(sort));
            }
        }
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    public void collectMetricsAfterXMLImport() {
        PackageMetrics.sort(packages);
        for (PackageMetrics mpackage : packages) {
            mpackage.collectMetricsAfterXMLImport();
        }
    }
}
