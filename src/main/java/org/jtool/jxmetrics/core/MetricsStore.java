/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxplatform.builder.TimeInfo;
import java.util.List;
import java.util.ArrayList;
import java.time.ZonedDateTime;

/**
 * Stores information of all projects.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsStore {
    
    private String name;
    private String target;
    private ZonedDateTime time;
    
    private List<ProjectMetrics> mprojects = new ArrayList<>();
    
    public MetricsStore(String name, String target) {
        this.name = name;
        this.target = target;
        this.time = TimeInfo.getCurrentTime();
    }
    
    public MetricsStore(String name, String target, String time) {
        this.name = name;
        this.target = target;
        this.time = ZonedDateTime.parse(time);
    }
    
    public String getName() {
        return name;
    }
    
    public String getTarget() {
        return target;
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
    
    void add(ProjectMetrics mproject) {
        mprojects.add(mproject);
    }
    
    public List<ProjectMetrics> getProjectMetrics() {
        return mprojects;
    }
}
