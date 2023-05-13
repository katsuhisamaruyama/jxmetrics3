/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxmetrics.measurement.CBO;
import org.jtool.jxmetrics.measurement.DIT;
import org.jtool.jxmetrics.measurement.Default;
import org.jtool.jxmetrics.measurement.IMetric;
import org.jtool.jxmetrics.measurement.LCOM;
import org.jtool.jxmetrics.measurement.LOC;
import org.jtool.jxmetrics.measurement.NOACL;
import org.jtool.jxmetrics.measurement.NOAMD;
import org.jtool.jxmetrics.measurement.NOC;
import org.jtool.jxmetrics.measurement.NOECL;
import org.jtool.jxmetrics.measurement.NOEFD;
import org.jtool.jxmetrics.measurement.NOEMD;
import org.jtool.jxmetrics.measurement.NOFD;
import org.jtool.jxmetrics.measurement.NOMD;
import org.jtool.jxmetrics.measurement.NOMDFD;
import org.jtool.jxmetrics.measurement.NOPFD;
import org.jtool.jxmetrics.measurement.NOPMD;
import org.jtool.jxmetrics.measurement.NOST;
import org.jtool.jxmetrics.measurement.RFC;
import org.jtool.jxmetrics.measurement.WMC;
import org.jtool.jxmetrics.measurement.ATFD;
import org.jtool.jxmetrics.measurement.TCC;
import org.jtool.jxmetrics.measurement.LAA;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

/**
 * Stores metric values.
 * 
 * @author Katsuhisa Maruyama
 */
public class Metrics {
    
    public static final IMetric[] SELECTABLE_METRICS = {
            new LOC(), new NOST(),
            new NOMD(), new NOFD(), new NOMDFD(), new NOPMD(), new NOPFD(),
            new NOACL(), new NOECL(), new NOAMD(), new NOEMD(), new NOEFD(),
            new CBO(), new DIT(), new NOC(), new RFC(), new WMC(), new LCOM(),
            new ATFD(), new TCC(), new LAA()
        };
    public static final IMetric DEFAULT_METRIC = new Default();
    
    public static IMetric getMetric(String name) {
        for (int i = 0; i < SELECTABLE_METRICS.length; i++) {
            if (SELECTABLE_METRICS[i].getName().equals(name)) {
                return SELECTABLE_METRICS[i];
            }
        }
        return null;
    }
    
    public static List<String> getAllMetricNames() {
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < SELECTABLE_METRICS.length; i++) {
            names.add(SELECTABLE_METRICS[i].getName());
        }
        return names;
    }
    
    protected String fqn;
    
    protected Map<String, Double> metricValues = new HashMap<String, Double>();
    
    protected Metrics(String fqn) {
        this.fqn = fqn;
    }
    
    protected void setCodeProperties(int start, int end, int upper, int bottom) {
    }
    
    public String getQualifiedName() {
        return fqn;
    }
    
    public double getMetricValueWithException(String sort) throws UnsupportedMetricsException {
        Double value = metricValues.get(sort);
        if (value != null) {
            return value.doubleValue();
        }
        throw new UnsupportedMetricsException("Cannot obtain the metric value of " + sort);
    }
    
    public double getMetricValue(String sort) {
        Double value = metricValues.get(sort);
        if (value != null) {
            return value.doubleValue();
        }
        return 0.0;
    }
    
    public void putMetricValue(String sort, double value) {
        metricValues.put(sort, value);
    }
    
    public Map<String, Double> getMetricValues() {
        return metricValues;
    }
    
    public void sortNames(List<String> names) {
        Collections.sort(names, new Comparator<>() {
            public int compare(String name1, String name2) {
                return name1.compareTo(name2);
            }
        });
    }
}
