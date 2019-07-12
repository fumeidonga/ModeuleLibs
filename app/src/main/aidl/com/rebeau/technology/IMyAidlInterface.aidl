// IMyAidlInterface.aidl
package com.rebeau.technology;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void test(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
