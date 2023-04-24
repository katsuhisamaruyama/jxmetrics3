/*
 *  Copyright 2023
 *  Software Science and Technology Lab., Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.srcmodel.JavaClass;
import org.jtool.srcmodel.JavaMethod;
import org.jtool.srcmodel.JavaField;
import org.eclipse.jdt.core.dom.Modifier;

/**
 * Stores metric values and information common to a class, method, and field.
 * 
 * @author Katsuhisa Maruyama
 */
public class CommonMetrics extends Metrics {
    
    protected String name;
    protected String type;
    protected int modifiers;
    
    protected int start = -1;
    protected int end = 0;
    protected int upper = -1;
    protected int bottom = 0;
    
    protected CommonMetrics(String fqn, String name, String type, int modifiers) {
        super(fqn);
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }
    
    @Override
    protected void setCodeProperties(int start, int end, int upper, int bottom) {
        this.start = start;
        this.end = end;
        this.upper = upper;
        this.bottom = bottom;
    }
    
    protected void setCodeProperties(JavaClass jclass) {
        this.start = jclass.getCodeRange().getStartPosition();
        this.end = jclass.getCodeRange().getEndPosition();
        this.upper = jclass.getCodeRange().getUpperLineNumber();
        this.bottom = jclass.getCodeRange().getBottomLineNumber();
    }
    
    protected void setCodeProperties(JavaMethod jmethod) {
        this.start = jmethod.getCodeRange().getStartPosition();
        this.end = jmethod.getCodeRange().getEndPosition();
        this.upper = jmethod.getCodeRange().getUpperLineNumber();
        this.bottom = jmethod.getCodeRange().getBottomLineNumber();
    }
    
    protected void setCodeProperties(JavaField jfield) {
        this.start = jfield.getCodeRange().getStartPosition();
        this.end = jfield.getCodeRange().getEndPosition();
        this.upper = jfield.getCodeRange().getUpperLineNumber();
        this.bottom = jfield.getCodeRange().getBottomLineNumber();
        
    }
    
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public int getModifiers() {
        return modifiers;
    }
    
    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }
    
    public boolean isProtected() {
        return Modifier.isProtected(modifiers);
    }
    
    public boolean isPrivate() {
        return Modifier.isPrivate(modifiers);
    }
    
    public boolean isDefault() {
        return !isPublic() && !isProtected() && !isPrivate();
    }
    
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }
    
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }
    
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }
    
    public boolean isStrictfp() {
        return Modifier.isStrictfp(modifiers);
    }
    
    public int getStartPosition() {
        return start;
    }
    
    public int getEndPosition() {
        return end;
    }
    
    public int getUpperLineNumber() {
        return upper;
    }
    
    public int getBottomLineNumber() {
        return bottom;
    }
    
    public String getSourceCode(String path) {
        String code = SourceCodeStore.getInstance().get(path);
        if (code != null && code.length() > 0 && end - start >= 0) {
            return code.substring(start, end + 1);
        }
        return "";
    }
}
