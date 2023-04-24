/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxmetrics.measurement.LOC;
import org.jtool.jxmetrics.measurement.NOST;
import org.jtool.srcmodel.JavaProject;
import org.jtool.srcmodel.JavaField;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Stores metric information on a field.
 * 
 * @author Katsuhisa Maruyama
 */
public class FieldMetrics extends CommonMetrics {
    
    public static final String Id = "FieldMetrics";
    
    protected FieldMetrics.Kind kind;
    protected ClassMetrics classMetrics;
    
    public enum Kind {
        J_FIELD, J_ENUM_CONSTANT, UNKNOWN;
    }
    
    protected FieldMetrics(String fqn, String name, String type, int modifiers) {
        super(fqn, name, type, modifiers);
    }
    
    public FieldMetrics(JavaProject jproject, JavaField jfield, ClassMetrics mclass) {
        this(jfield.getQualifiedName().fqn(), jfield.getName(), jfield.getType(), jfield.getModifiers());
        
        kind = getKind(jfield);
        classMetrics = mclass;
        
        int start = jfield.getCodeRange().getStartPosition();
        int end = jfield.getCodeRange().getEndPosition();
        int upper = jfield.getCodeRange().getUpperLineNumber();
        int bottom = jfield.getCodeRange().getBottomLineNumber();
        setCodeProperties(start, end, upper, bottom);
        
        collectMetrics(jproject, jfield);
    }
    
    private FieldMetrics.Kind getKind(JavaField jfield) {
        if (jfield.isField()) {
            return FieldMetrics.Kind.J_FIELD;
        } else if (jfield.isEnumConstant()) {
            return FieldMetrics.Kind.J_ENUM_CONSTANT;
        } else {
            return FieldMetrics.Kind.UNKNOWN;
        }
    }
    
    public FieldMetrics(String fqn, String name, String type, int modifiers, String kindStr, ClassMetrics mclass) {
        super(fqn, name, type, modifiers);
        
        this.kind = FieldMetrics.Kind.valueOf(kindStr);
        classMetrics = mclass;
    }
    
    public ClassMetrics getDeclaringClass() {
        return classMetrics;
    }
    
    public String getDeclaringClassName() {
        return classMetrics.getQualifiedName();
    }
    
    public FieldMetrics.Kind getKind() {
        return kind;
    }
    
    public boolean isField() {
        return kind == FieldMetrics.Kind.J_FIELD;
    }
    
    public boolean isEnumConstant() {
        return kind == FieldMetrics.Kind.J_ENUM_CONSTANT;
    }
    
    public String getSourceCode() {
        return super.getSourceCode(classMetrics.getFullPath());
    }
    
    protected void collectMetrics(JavaProject jproject, JavaField jfield) {
        putMetricValue(LOC.Name, new LOC().calculate(jfield));
        putMetricValue(NOST.Name,new NOST().calculate(jfield));
    }
    
    public static void sort(List<FieldMetrics> mfields) {
        Collections.sort(mfields, new Comparator<FieldMetrics>() {
            
            @Override
            public int compare(FieldMetrics mfield1, FieldMetrics mfield2) {
                return mfield1.getName().compareTo(mfield2.getName());
            }
        });
    }
}
