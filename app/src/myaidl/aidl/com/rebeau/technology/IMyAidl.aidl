// IMyReleaseAidlInterface.aidl
package com.rebeau.technology;

// Declare any non-default types here with import statements

interface IMyAidl {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void request11(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
