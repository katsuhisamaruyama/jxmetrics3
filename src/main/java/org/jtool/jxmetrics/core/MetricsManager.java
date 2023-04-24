/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxmetrics.Logger;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaPackage;
import org.jtool.jxmetrics.XMLWriter;
import org.jtool.jxplatform.builder.ConsoleProgressMonitor;
import org.jtool.jxplatform.builder.TimeInfo;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Calculates the metric values related to elements within a project.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsManager {
    
    private static final String JXMETRICS_PREFIX = "JX-";
    private static final String XML_FILENAME_EXT = ".xml";
    
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
    
    public MetricsManager() {
    }
    
    public ProjectMetrics calculate(JavaProject jproject) {
        ZonedDateTime time = TimeInfo.getCurrentTime();
        ProjectMetrics mproject =  new ProjectMetrics(jproject, time);
        
        int size = jproject.getPackages().size();
        Logger.getInstance().printLog("** Ready to calculate the metric values of 1 project and " + size + " packages");
        ConsoleProgressMonitor pm = new ConsoleProgressMonitor();
        pm.begin(size);
        for (JavaPackage jpackage : jproject.getPackages()) {
            PackageMetrics mpackage = new PackageMetrics(jproject, jpackage, mproject);
            mproject.addPackage(mpackage);
            pm.work(1);
            Logger.getInstance().printLog("-Calculated " + mpackage.getName());
        }
        pm.done();
        mproject.collect(jproject);
        return mproject;
    }
    
    public void exportXML(ProjectMetrics mproject, String path) {
        if (path.length() == 0) {
            path = JXMETRICS_PREFIX + "-" + mproject.getName() + "-" + String.valueOf(mproject.getTimeAsLong()) + XML_FILENAME_EXT;
        }
        File file = new File(mproject.getPath() + File.separatorChar + path);
        if (file.exists()) {
            file.delete();
        }
        
        Logger.getInstance().printLog("** Ready to export data " + mproject.getName());
        MetricDataExporter exporter = new MetricDataExporter();
        Document doc = exporter.getDocument(mproject);
        XMLWriter.write(file, doc);
        Logger.getInstance().printLog("-Exported " + path);
    }
    
    public ProjectMetrics importXML(String path) {
        if (path == null || path.length() == 0) {
            return null;
        }
        return importXML(new File(path));
    }
    
    public ProjectMetrics importXML(File file) {
        if (file.canRead() && file.getPath().endsWith(XML_FILENAME_EXT)) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser parser = factory.newSAXParser();
                MetricDataImporter handler = new MetricDataImporter();
                parser.parse(file, handler);
                return handler.getProjectMetrics();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
