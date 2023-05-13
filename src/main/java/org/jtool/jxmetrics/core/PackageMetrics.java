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
import org.jtool.jxmetrics.measurement.NOAPG;
import org.jtool.jxmetrics.measurement.NOEPG;
import org.jtool.jxmetrics.measurement.Metric;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaPackage;
import org.jtool.srcmodel.JavaClass;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Stores metric information on a package.
 * 
 * @author Katsuhisa Maruyama
 */
public class PackageMetrics extends Metrics {
    
    public static final String Id = "PackageMetrics";
    
    private ProjectMetrics projectMetrics;
    
    protected List<ClassMetrics> classes = new ArrayList<>();
    protected List<String> afferentPackageNames = new ArrayList<>();
    protected List<String> efferentPackageNames = new ArrayList<>();
    
    public PackageMetrics(JavaProject jproject, JavaPackage jpackage, ProjectMetrics mproject) {
        super(jpackage.getName());
        
        projectMetrics = mproject;
        for (JavaClass jclass : new ArrayList<>(jpackage.getClasses())) {
            ClassMetrics mclass = new ClassMetrics(jproject, jclass, this);
            classes.add(mclass);
        }
        
        for (JavaPackage jp : jpackage.getAfferentJavaPackages()) {
            addAfferentPackage(jp.getName());
        }
        for (JavaPackage jp : jpackage.getEfferentJavaPackages()) {
            addEfferentPackage(jp.getName());
        }
        
        ClassMetrics.sort(classes);
        sortNames(afferentPackageNames);
        sortNames(efferentPackageNames);
        collectMetrics(jpackage);
        collectMetricsMax();
    }
    
    public String getName() {
        return fqn;
    }
    
    public PackageMetrics(String name, ProjectMetrics mproject) {
        super(name);
        projectMetrics = mproject;
    }
    
    public ProjectMetrics getProject() {
        return projectMetrics;
    }
    
    protected void addClass(ClassMetrics mclass) {
        classes.add(mclass);
    }
    
    public List<ClassMetrics> getClasses() {
        return classes;
    }
    
    public void sortClasses() {
        ClassMetrics.sort(classes);
    }
    
    protected void addAfferentPackage(String name) {
        if (!afferentPackageNames.contains(name)) {
            afferentPackageNames.add(name);
        }
    }
    
    public List<String> getAfferentPackages() {
        return afferentPackageNames;
    }
    
    protected void addEfferentPackage(String name) {
        if (!efferentPackageNames.contains(name)) {
            efferentPackageNames.add(name);
        }
    }
    
    public List<String> getEfferentPackages() {
        return efferentPackageNames;
    }
    
    protected void collectMetrics(JavaPackage jpackage) {
        putSumMetricValue(LOC.Name);
        putSumMetricValue(NOST.Name);
        
        putMetricValue(NOC.Name, new NOCL().calculate(jpackage));
        
        putSumMetricValue(NOMD.Name);
        putSumMetricValue(NOFD.Name);
        putSumMetricValue(NOMDFD.Name);
        putSumMetricValue(NOPMD.Name);
        putSumMetricValue(NOPFD.Name);
        
        putMetricValue(NOAPG.Name, new NOAPG().calculate(jpackage));
        putMetricValue(NOEPG.Name, new NOEPG().calculate(jpackage));
        
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
    
    protected Double sum(String sort) {
        double value = 0;
        for (ClassMetrics mclass : classes) {
            value = value + mclass.getMetricValue(sort);
        }
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    protected Double max(String sort) {
        double value = 0;
        for (ClassMetrics mclass : classes) {
            value = Math.max(value, mclass.getMetricValue(sort));
        }
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    public static void sort(List<PackageMetrics> packages) {
        Collections.sort(packages, new Comparator<>() {
            
            @Override
            public int compare(PackageMetrics mpackage1, PackageMetrics mpackage2) {
                return mpackage1.getName().compareTo(mpackage2.getName());
            }
        });
    }
    
    public void collectMetricsAfterXMLImport() {
        ClassMetrics.sort(classes);
        sortNames(afferentPackageNames);
        sortNames(efferentPackageNames);
        for (ClassMetrics mclass : classes) {
            mclass.collectMetricsAfterXMLImport();
        }
    }
}
