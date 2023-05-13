/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxplatform.builder.ModelBuilderBatch;
import org.jtool.jxplatform.builder.ConsoleProgressMonitor;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaPackage;
import org.jtool.jxmetrics.Logger;
import org.jtool.jxmetrics.XMLWriter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calculates the metric values related to elements within a project.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsManager {
    
    public static final String JXMETRICS_PREFIX = "jxm";
    public static final String XML_FILENAME_EXT = ".xml";
    public static final String TIME_BOILERPLATE = "<time>";
    
    public static final String ProjectsElem = "projects";
    public static final String ProjectElem = "project";
    public static final String PackageElem = "package";
    public static final String ClassElem = "class";
    public static final String MethodElem = "method";
    public static final String FieldElem = "field";
    
    public static final String SuperClassElem = "superClass";
    public static final String SuperInterfaceElem = "superInterface";
    public static final String AfferentElem = "afferent";
    public static final String EfferentElem = "efferent";
    
    public static final String MetricsElem = "metrics";
    
    public static final String CodeElem = "code";
    public static final String StartPositionAttr = "start";
    public static final String EndPositionAttr = "end";
    public static final String UpperLineNumberAttr = "upper";
    public static final String BottomLineNumberAttr = "bottom";
    
    public static final String FqnAttr = "fqn";
    public static final String NameAttr = "name";
    public static final String TypeAttr = "type";
    public static final String ModifiersAttr = "modifiers";
    public static final String KindAttr = "kind";
    public static final String PathAttr = "path";
    public static final String TimeAttr = "time";
    
    public static final String Yes = "yes";
    public static final String No = "no";
    
    private ModelBuilderBatch builder;
    
    public MetricsManager() {
    }
    
    public MetricsStore calculate(String name, String target, boolean logging) {
        MetricsStore mstore = new MetricsStore(name, target);
        builder = new ModelBuilderBatch(false, false);
        builder.setConsoleVisible(logging);
        
        List<JavaProject> jprojects = builder.build(name, target);
        List<JavaPackage> jpackages = jprojects.stream()
                .flatMap(pj -> pj.getPackages().stream())
                .filter(jp -> jp.isInProject())
                .collect(Collectors.toList());
        
        int size = jpackages.size();
        Logger.getInstance().printLog("** Ready to calculate the metric values of " + size + " packages");
        ConsoleProgressMonitor pm = new ConsoleProgressMonitor();
        pm.begin(size);
        for (JavaProject jproject : jprojects) {
            ProjectMetrics mproject =  new ProjectMetrics(jproject);
            for (JavaPackage jpackage : jproject.getPackages()) {
                if (jpackage.isInProject()) {
                    PackageMetrics mpackage = new PackageMetrics(jproject, jpackage, mproject);
                    mproject.addPackage(mpackage);
                    pm.work(1);
                }
            }
            mproject.collect(jproject);
            mstore.add(mproject);
        }
        pm.done();
        return mstore;
    }
    
    public void unbuild() {
        if (builder != null) {
            builder.unbuild();
        }
    }
    
    public void exportXML(MetricsStore mstore, String output) {
        if (mstore.getProjectMetrics().size() == 0) {
            System.err.print("No project");
            return;
        }
        
        long time = mstore.getTimeAsLong();
        output = output.replace(TIME_BOILERPLATE, String.valueOf(time));
        File file = new File(output);
        if (file.exists()) {
            file.delete();
        }
        
        try {
            Logger.getInstance().printLog("** Ready to export data into " + output);
            MetricDataExporter exporter = new MetricDataExporter();
            Document doc = exporter.getDocument(mstore);
            XMLWriter.write(file, doc);
            Logger.getInstance().printLog("-Exported ");
        } catch (ParserConfigurationException e) {
            System.err.println("-Failed to export: " + e.getMessage() + ".");
        }
    }
    
    public MetricsStore importXML(String filename) {
        if (filename == null || filename.length() == 0) {
            System.err.println("No such file: " + filename);
            return null;
        }
        
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            System.err.println("No such file: " + filename);
        }
        return importXML(path.toFile());
    }
    
    public MetricsStore importXML(File file) {
        if (file.canRead() && file.getPath().endsWith(XML_FILENAME_EXT)) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser parser = factory.newSAXParser();
                MetricDataImporter importer = new MetricDataImporter();
                parser.parse(file, importer);
                return importer.getJxmProjects();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
