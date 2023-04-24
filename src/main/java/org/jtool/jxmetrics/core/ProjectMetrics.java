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
import org.jtool.jxplatform.builder.TimeInfo;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

/**
 * Stores metric information on a project.
 * 
 * @author Katsuhisa Maruyama
 */
public class ProjectMetrics extends Metrics {
    
    public static final String Id = "ProjectMetrics";
    
    protected String path;
    protected ZonedDateTime time;
    
    protected List<PackageMetrics> packages = new ArrayList<PackageMetrics>();
    
    public ProjectMetrics(JavaProject jproject, ZonedDateTime time) {
        super(jproject.getName());
        
        path = jproject.getTopPath();
        this.time = time;
    }
    
    public void collect(JavaProject jproject) {
        PackageMetrics.sort(packages);
        collectMetrics(jproject);
        collectMetricsMax();
    }
    
    public ProjectMetrics(String name, String path, ZonedDateTime time) {
        super(name);
        this.path = path;
        this.time = time;
    }
    
    public String getName() {
        return fqn;
    }
    
    public String getPath() {
        return path;
    }
    
    public ZonedDateTime getTime() {
        return time;
    }
    
    public long getTimeAsLong() {
        return TimeInfo.getTimeAsLong(time);
    }
    
    public String getTimeAsString() {
        return TimeInfo.getTimeAsISOString(time);
    }
    
    public String getFormatedDate() {
        return TimeInfo.getFormatedDate(time);
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
    
    public static void sort(List<ProjectMetrics> projects) {
        Collections.sort(projects, new Comparator<ProjectMetrics>() {
            
            @Override
            public int compare(ProjectMetrics project1, ProjectMetrics project2) {
                long time1 = project1.getTimeAsLong();
                long time2 = project2.getTimeAsLong();
                if (time1 > time2) {
                    return 1;
                } else if (time1 < time2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
}
