package com.kalaari.hibernatetypes;

import java.util.Properties;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import com.vladmihalcea.hibernate.type.array.internal.ArraySqlTypeDescriptor;
import com.vladmihalcea.hibernate.type.array.internal.StringArrayTypeDescriptor;

public class StringArrayType extends AbstractSingleColumnStandardBasicType<String[]>
        implements DynamicParameterizedType {

    public StringArrayType() {
        super(ArraySqlTypeDescriptor.INSTANCE, StringArrayTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "text-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((StringArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}