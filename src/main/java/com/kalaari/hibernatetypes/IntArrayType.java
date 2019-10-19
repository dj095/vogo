package com.kalaari.hibernatetypes;

import java.util.Properties;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

public class IntArrayType extends AbstractSingleColumnStandardBasicType<Long[]> implements DynamicParameterizedType {

    public IntArrayType() {
        super(ArraySqlTypeDescriptor.INSTANCE, BigIntArrayTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "bigint-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((BigIntArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}