package com.elephtribe.tools.dataprovider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Bytes on 2017/7/26.
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataModel
{
    public static final int File = 1;
    public static final int Method = 2;

    public abstract int value();
}
