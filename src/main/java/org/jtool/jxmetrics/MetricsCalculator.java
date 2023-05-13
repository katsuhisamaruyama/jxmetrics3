/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics;

import org.jtool.jxmetrics.core.MetricsStore;
import org.jtool.jxmetrics.core.MetricsManager;
import org.jtool.jxplatform.builder.CommandLineOptions;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * Calculates metric values.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsCalculator {
    
    public static void main(String[] args) {
        MetricsCalculator calculator  = new MetricsCalculator();
        calculator.run(args);
    }
    
    private void run(String[] args) {
        String cdir = new File(".").getAbsolutePath();
        int index = cdir.lastIndexOf(File.separatorChar);
        cdir = cdir.substring(0, index);
        
        CommandLineOptions options = new CommandLineOptions(args);
        
        String target = options.get("-target", ".");
        if (!target.startsWith(File.separator)) {
            if (target.startsWith(".")) {
                target = cdir;
            } else {
                target = cdir + File.separatorChar + target;
            }
        }
        
        if (!Files.exists(Paths.get(target))) {
            System.err.println("No such target folder");
            return;
        }
        
        String name = options.get("-name", null);
        if (name == null) {
            index = target.lastIndexOf(File.separatorChar);
            name = target.substring(index + 1);
        }
        
        String output = options.get("-output", null);
        if (output == null) {
            output = MetricsManager.JXMETRICS_PREFIX + "-" + name + 
                    "-" + MetricsManager.TIME_BOILERPLATE + MetricsManager.XML_FILENAME_EXT;
        }
        if (!output.startsWith(File.separator)) {
            output = target + File.separatorChar + output;
        }
        
        boolean logging = options.get("-logging", "on").equals("on") ? true : false;
        
        MetricsManager manager = new MetricsManager();
        MetricsStore mstore = manager.calculate(name, target, logging);
        manager.exportXML(mstore, output);
        manager.unbuild();
    }
}
