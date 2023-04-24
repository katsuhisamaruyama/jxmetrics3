/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics;

import org.jtool.jxmetrics.core.MetricsManager;
import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxplatform.builder.ModelBuilderBatch;
import org.jtool.jxplatform.builder.CommandLineOptions;
import org.jtool.srcmodel.JavaProject;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Calculates metric values.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsCalculator {
    
    private MetricsManager manager;
    
    public static void main(String[] args) {
        MetricsCalculator calculator  = new MetricsCalculator();
        calculator.run(args);
    }
    
    public MetricsCalculator() {
    }
    
    private void run(String[] args) {
        CommandLineOptions options = new CommandLineOptions(args);
        String target = options.get("-target", ".");
        String name = options.get("-name", target);
        if (!target.startsWith(File.separator)) {
            String cdir = new File(".").getAbsoluteFile().getParent();
            target = cdir + File.separatorChar + target;
        }
        String output = options.get("-output", null);
        boolean logging = options.get("-logging", "on").equals("on") ? true : false;
        
        List<ProjectMetrics> mprojects = getProjectMetrics(target, output, name, logging);
        if (output != null) {
            for (ProjectMetrics mproject : mprojects) {
                manager.exportXML(mproject, output);
            }
        }
    }
    
    public List<ProjectMetrics> getProjectMetrics(String target, String output, String name, boolean logging) {
        ModelBuilderBatch builder = new ModelBuilderBatch(false, false);
        List<JavaProject> jprojects = builder.build(name, target);
        manager = new MetricsManager();
        
        List<ProjectMetrics> mprojects = new ArrayList<>();
        for (JavaProject jproject : jprojects) {
            ProjectMetrics mproject = manager.calculate(jproject);
            mprojects.add(mproject);
        }
        builder.unbuild();
        return mprojects;
    }
}
