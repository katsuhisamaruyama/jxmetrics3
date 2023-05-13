/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Imports the metric values related to element within a project from an XML file.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricDataImporter extends DefaultHandler {
    
    private MetricsStore mstore;
    private ProjectMetrics mproject;
    private PackageMetrics mpackage;
    private ClassMetrics mclass;
    private MethodMetrics mmethod;
    private FieldMetrics mfield;
    private Metrics parent;
    
    public MetricDataImporter() {
    }
    
    public MetricsStore getJxmProjects() {
        return mstore;
    }
    
    @Override
    public void startDocument() throws SAXException {
    }
    
    @Override
    public void endDocument() throws SAXException {
        mstore.getProjectMetrics().forEach(p -> p.collectMetricsAfterXMLImport());
    }
    
    @Override
    public void startElement(String uri, String name, String qname, Attributes attrs) {
        if (qname.equals(MetricsManager.ProjectsElem)) {
            setJxmProjectAttributes(attrs);
            return;
        }
        
        if (qname.equals(MetricsManager.ProjectElem)) {
            setProjectAttributes(attrs);
            parent = mproject;
            return;
        }
        
        if (qname.equals(MetricsManager.PackageElem)) {
            setPackageAttributes(attrs);
            parent = mpackage;
            return;
        }
        
        if (qname.equals(MetricsManager.ClassElem)) {
            setClassAttributes(attrs);
            parent = mclass;
            return;
        }
        
        if (qname.equals(MetricsManager.MethodElem)) {
            setMethodAttributes(attrs);
            parent = mmethod;
            return;
        }
        
        if (qname.equals(MetricsManager.FieldElem)) {
            setFieldAttributes(attrs);
            parent = mfield;
            return;
        }
        
        if (qname.equals(MetricsManager.SuperClassElem)) {
            if (parent == mclass) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    mclass.setSuperClass(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.SuperInterfaceElem)) {
            if (parent == mclass) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    mclass.addSuperInterface(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.AfferentElem)) {
            if (parent == mpackage) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    mpackage.addAfferentPackage(attrs.getValue(0));
                }
            } else if (parent == mclass) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    mclass.addAfferentClass(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.EfferentElem)) {
            if (parent == mpackage) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    mpackage.addEfferentPackage(attrs.getValue(0));
                }
            } else if (parent == mclass) {
                if (attrs.getQName(0).equals(MetricsManager.FqnAttr)) {
                    mclass.addEfferentClass(attrs.getValue(0));
                }
            }
            return;
        }
        
        if (qname.equals(MetricsManager.MetricsElem)) {
            setMetricAttributes(attrs);
            return;
        }
        
        if (qname.equals(MetricsManager.CodeElem)) {
            setCodeAttributes(attrs);
            return;
        }
    }
    
    @Override
    public void endElement(String uri, String name, String qname) {
        if (qname.equals(MetricsManager.ProjectElem)) {
            parent = null;
            return;
        }
        
        if (qname.equals(MetricsManager.PackageElem)) {
            parent = mproject;
            return;
        }
        
        if (qname.equals(MetricsManager.ClassElem)) {
            parent = mpackage;
            return;
        }
        
        if (qname.equals(MetricsManager.MethodElem)) {
            parent = mclass;
            return;
        }
        
        if (qname.equals(MetricsManager.FieldElem)) {
            parent = mclass;
            return;
        }
    }
    
    private void setJxmProjectAttributes(Attributes attrs) {
        String name = null;
        String target = null;
        String time = null;
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.PathAttr)) {
                target = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.TimeAttr)) {
                time = attrs.getValue(i);
            }
        }
        
        if (name != null && target != null && time != null) {
            mstore = new MetricsStore(name, target, time);
        }
    }
    
    private void setProjectAttributes(Attributes attrs) {
        String name = null;
        String path = null;
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.PathAttr)) {
                path = attrs.getValue(i);
            }
        }
        if (name != null && path != null) {
            mproject = new ProjectMetrics(name, path);
            mstore.add(mproject);
        }
    }
    
    private void setPackageAttributes(Attributes attrs) {
        String name = null;
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            }
        }
        if (name != null) {
            mpackage = new PackageMetrics(name, mproject);
            mproject.addPackage(mpackage);
        }
    }
    
    private void setClassAttributes(Attributes attrs) {
        String fqn = null;
        String name = null;
        int modifiers = 0;
        String path = null;
        String kindStr = null;
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.FqnAttr)) {
                fqn = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.ModifiersAttr)) {
                modifiers = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.PathAttr)) {
                path = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.KindAttr)) {
                kindStr = attrs.getValue(i);
            }
        }
        
        if (fqn != null) {
            mclass = new ClassMetrics(fqn, name, modifiers, kindStr, path, mpackage);
            mpackage.addClass(mclass);
        }
    }
    
    private void setMethodAttributes(Attributes attrs) {
        String fqn = null;
        String name = null;
        String type = null;
        int modifiers = 0;
        String kindStr = null;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.FqnAttr)) {
                fqn = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            }  else if (attrs.getQName(i).equals(MetricsManager.TypeAttr)) {
                type = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.ModifiersAttr)) {
                modifiers = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.KindAttr)) {
                kindStr = attrs.getValue(i);
            }
        }
        
        if (fqn != null) {
            mmethod = new MethodMetrics(fqn, name, type, modifiers, kindStr, mclass);
            mclass.addMethod(mmethod);
        }
    }
    
    private void setFieldAttributes(Attributes attrs) {
        String fqn = null;
        String name = null;
        String type = null;
        int modifiers = 0;
        String kindStr = null;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.FqnAttr)) {
                fqn = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.NameAttr)) {
                name = attrs.getValue(i);
            }  else if (attrs.getQName(i).equals(MetricsManager.TypeAttr)) {
                type = attrs.getValue(i);
            } else if (attrs.getQName(i).equals(MetricsManager.ModifiersAttr)) {
                modifiers = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.KindAttr)) {
                kindStr = attrs.getValue(i);
            }
        }
        
        if (fqn != null) {
            mfield = new FieldMetrics(fqn, name, type, modifiers, kindStr, mclass);
            mclass.addField(mfield);
        }
    }
    
    private void setMetricAttributes(Attributes attrs) {
        if (parent == null) {
            return;
        }
        for (int i = 0; i < attrs.getLength(); i++) {
            parent.putMetricValue(attrs.getQName(i), Double.parseDouble(attrs.getValue(i)));
        }
    }
    
    private void setCodeAttributes(Attributes attrs) {
        if (parent == null) {
            return;
        }
        
        int start = -1;
        int end = 0;
        int upper = -1;
        int bottom = 0;
        
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.getQName(i).equals(MetricsManager.StartPositionAttr)) {
                start = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.EndPositionAttr)) {
                end = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.UpperLineNumberAttr)) {
                upper = getInteger(attrs.getValue(i));
            } else if (attrs.getQName(i).equals(MetricsManager.BottomLineNumberAttr)) {
                bottom = getInteger(attrs.getValue(i));
            }
        }
        
        if (start >= 0 && upper >= 0) {
            parent.setCodeProperties(start, end, upper, bottom);
        }
    }
    
    @SuppressWarnings("unused")
    private boolean getBoolean(String value) {
        return value.equals(MetricsManager.Yes);
    }
    
    private int getInteger(String value) {
        return Integer.parseInt(value);
    }
    
    @SuppressWarnings("unused")
    private long getLong(String value) {
        return Long.parseLong(value);
    }
}
