/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxmetrics.measurement.LOC;
import org.jtool.jxmetrics.measurement.NOST;
import org.jtool.jxmetrics.measurement.CYCLO;
import org.jtool.jxmetrics.measurement.NEST;
import org.jtool.jxmetrics.measurement.PAR;
import org.jtool.jxmetrics.measurement.LVAR;
import org.jtool.jxmetrics.measurement.NOAMD;
import org.jtool.jxmetrics.measurement.NOEMD;
import org.jtool.jxmetrics.measurement.NOEFD;
import org.jtool.jxmetrics.measurement.ATFD;
import org.jtool.jxmetrics.measurement.FDP;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaMethod;
import org.jtool.srcmodel.JavaField;
import org.jtool.cfg.CFG;
import org.jtool.cfg.CFGMethodEntry;
import org.jtool.cfg.CFGNode;
import org.jtool.cfg.CFGStatement;
import org.jtool.cfg.JReference;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Stores metric information on a method.
 * 
 * @author Katsuhisa Maruyama
 */
public class MethodMetrics extends CommonMetrics {
    
    public static final String Id = "MethodMetrics";
    
    protected Kind kind;
    protected ClassMetrics classMetrics;
    
    public enum Kind {
        J_METHOD, J_CONSTRUCTOR, J_INITIALIZER, J_LAMBDA, UNKNOWN;
    }
    
    protected MethodMetrics(String fqn, String name, String type, int modifiers) {
        super(fqn, name, type, modifiers);
    }
    
    public MethodMetrics(JavaProject jproject, JavaMethod jmethod, ClassMetrics mclass) {
        this(jmethod.getQualifiedName().fqn(), jmethod.getSignature(),
                jmethod.getReturnType(), jmethod.getModifiers());
        
        kind = getKind(jmethod);
        classMetrics = mclass;
        setCodeProperties(jmethod);
        
        collectMetrics(jproject, jmethod);
    }
    
    private MethodMetrics.Kind getKind(JavaMethod jmethod) {
        if (jmethod.isMethod()) {
            return MethodMetrics.Kind.J_METHOD;
        } else if (jmethod.isConstructor()) {
            return MethodMetrics.Kind.J_CONSTRUCTOR;
        } else if (jmethod.isInitializer()) {
            return MethodMetrics.Kind.J_INITIALIZER;
        } else if (jmethod.isLambda()) {
            return MethodMetrics.Kind.J_LAMBDA;
        } else {
            return MethodMetrics.Kind.UNKNOWN;
        }
    }
    
    public MethodMetrics(String fqn, String name, String type,
            int modifiers, String kindStr, ClassMetrics mclass) {
        super(fqn, name, type, modifiers);
        kind = MethodMetrics.Kind.valueOf(kindStr);
        classMetrics = mclass;
    }
    
    public String getReturnType() {
        return type;
    }
    
    public String getSignature() {
        return name;
    }
    
    public ClassMetrics getDeclaringClass() {
        return classMetrics;
    }
    
    public String getDeclaringClassName() {
        return classMetrics.getQualifiedName();
    }
    
    public MethodMetrics.Kind getKind() {
        return kind;
    }
    
    public boolean isMethod() {
        return kind == MethodMetrics.Kind.J_METHOD;
    }
    
    public boolean isConstructor() {
        return kind == MethodMetrics.Kind.J_CONSTRUCTOR;
    }
    
    public boolean isInitializer() {
        return kind == MethodMetrics.Kind.J_INITIALIZER;
    }
    
    public boolean isLambda() {
        return kind == MethodMetrics.Kind.J_LAMBDA;
    }
    
    public String getSourceCode() {
        return super.getSourceCode(classMetrics.getFullPath());
    }
    
    protected void collectMetrics(JavaProject jproject, JavaMethod jmethod) {
        putMetricValue(LOC.Name, new LOC().calculate(jmethod));
        putMetricValue(NOST.Name, new NOST().calculate(jmethod));
        
        putMetricValue(NOAMD.Name, new NOAMD().calculate(jmethod));
        putMetricValue(NOEMD.Name, new NOEMD().calculate(jmethod));
        putMetricValue(NOEFD.Name, new NOEFD().calculate(jmethod));
        
        putMetricValue(CYCLO.Name, new CYCLO().calculate(jmethod));
        putMetricValue(LVAR.Name, new LVAR().calculate(jmethod));
        putMetricValue(NEST.Name, new NEST().calculate(jmethod));
        putMetricValue(PAR.Name, new PAR().calculate(jmethod));
        
        putMetricValue(ATFD.Name, new ATFD().calculate(jproject, jmethod));
        putMetricValue(FDP.Name, new FDP().calculate(jproject, jmethod));
    }
    
    public static Set<String> getAccessedFields(JavaProject jproject, JavaMethod jmethod) {
        Set<String> accessedFields = new HashSet<String>();
        
        for (JavaField jf : jmethod.getAccessedFieldsInProject()) {
            String declaringClass = jf.getDeclaringClass().getQualifiedName().fqn();
            if (!jmethod.getDeclaringClass().getQualifiedName().fqn().equals(declaringClass)) {
                accessedFields.add(jf.getName());
            }
        }
        for (JavaMethod jm : jmethod.getCalledMethodsInProject()) {
            JavaField jf = MethodMetrics.getFieldbyAccessor(jproject, jm);
            if (jf != null) {
                String declaringClass = jf.getDeclaringClass().getQualifiedName().fqn();
                if (!jmethod.getDeclaringClass().getQualifiedName().fqn().equals(declaringClass)) {
                    accessedFields.add(jf.getName());
                }
            }
        }
        return accessedFields;
    }
    
    public static Set<String> getAccessedClasses(JavaProject jproject, JavaMethod jmethod) {
        Set<String> accessedClasses = new HashSet<String>();
        
        for (JavaField jf : jmethod.getAccessedFieldsInProject()) {
            String declaringClass = jf.getDeclaringClass().getQualifiedName().fqn();
            if (!jmethod.getDeclaringClass().getQualifiedName().fqn().equals(declaringClass)) {
                accessedClasses.add(declaringClass);
            }
        }
        for (JavaMethod jm : jmethod.getCalledMethodsInProject()) {
            JavaField jf = MethodMetrics.getFieldbyAccessor(jproject, jm);
            if (jf != null) {
                String declaringClass = jf.getDeclaringClass().getQualifiedName().fqn();
                if (!jmethod.getDeclaringClass().getQualifiedName().fqn().equals(declaringClass)) {
                    accessedClasses.add(declaringClass);
                }
            }
        }
        return accessedClasses;
    }
    
    public static JavaField getFieldbyAccessor(JavaProject jproject, JavaMethod jmethod) {
        if (isSetter(jproject, jmethod)) {
            return jmethod.getAccessedFields().iterator().next();
        } else if (isGetter(jproject, jmethod)) {
            return jmethod.getAccessedFields().iterator().next();
        }
        return null;
    }
    
    public static boolean isAccessor(JavaProject jproject, JavaMethod jmethod) {
        return isSetter(jproject, jmethod) || isGetter(jproject, jmethod);
    }
    
    public static boolean isSetter(JavaProject jproject, JavaMethod jmethod) {
        if (jmethod.isPrivate() || jmethod.getParameterSize() != 1 || jmethod.getAccessedFields().size() != 1) {
            return false;
        }
        
        CFG cfg = jproject.getModelBuilder().getCFG(jmethod);
        CFGMethodEntry entry = (CFGMethodEntry)cfg.getEntryNode();
        JReference param = entry.getFormalIn(0).getDefVariable();
        
        int num = 0;
        if (cfg.getNodes().size() == 4) {
            for (CFGNode node : cfg.getNodes()) {
                if (node.isAssignment()) {
                    CFGStatement stNode = (CFGStatement)node;
                    if (stNode.getDefVariables().size() == 1 && stNode.getUseVariables().size() == 1 &&
                            stNode.getUseVariables().get(0).getQualifiedName().equals(param.getQualifiedName()) &&
                            stNode.getDefVariables().get(0).isFieldAccess()) {
                        num++;
                    }
                }
            }
        }
        return num == 1;
    }
    
    public static boolean isGetter(JavaProject jproject, JavaMethod jmethod) {
        if (jmethod.getParameterSize() != 0 || jmethod.getAccessedFields().size() != 1) {
            return false;
        }
        
        CFG cfg = jproject.getModelBuilder().getCFG(jmethod);
        int num = 0;
        if (cfg.getNodes().size() == 4) {
            for (CFGNode node : cfg.getNodes()) {
                if (node.isReturn()) {
                    CFGStatement stNode = (CFGStatement)node;
                    if (stNode.getUseVariables().size() == 1) {
                        num++;
                    }
                }
            }
        }
        return num == 1;
    }
    
    public static void sort(List<MethodMetrics> methods) {
        Collections.sort(methods, new Comparator<>() {
            
            @Override
            public int compare(MethodMetrics method1, MethodMetrics method2) {
                return method1.getSignature().compareTo(method2.getSignature());
            }
        });
    }
    
    public static int getNumberOfStatements(JavaMethod jmethod) {
        if (jmethod.getASTNode() != null) {
            StatementCollector statementCollector = new StatementCollector();
            jmethod.getASTNode().accept(statementCollector);
            return statementCollector.getNumberOfStatements();
        }
        return 0;
    }
    
    public static int getMaximumNumberOfNesting(JavaMethod jmethod) {
        if (jmethod.getASTNode() != null) {
            StatementCollector statementCollector = new StatementCollector();
            jmethod.getASTNode().accept(statementCollector);
            return statementCollector.getMaximumNuberOfNesting();}
        return 0;
    }
    
    public static int getCyclomaticNumber(JavaMethod jmethod) {
        if (jmethod.getASTNode() != null) {
            StatementCollector statementCollector = new StatementCollector();
            jmethod.getASTNode().accept(statementCollector);
            return statementCollector.getCyclomaticNumber();
        }
        return 0;
    }
}
