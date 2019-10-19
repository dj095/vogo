package com.kalaari.hibernatetypes;

import com.vladmihalcea.hibernate.type.array.internal.AbstractArrayTypeDescriptor;

public class BigIntArrayTypeDescriptor extends AbstractArrayTypeDescriptor<Long[]> {

    public static final BigIntArrayTypeDescriptor INSTANCE = new BigIntArrayTypeDescriptor();

    public BigIntArrayTypeDescriptor() {
        super(Long[].class);
    }

    @Override
    protected String getSqlArrayType() {
        return "bigint";
    }
}