
java.lang.ClassCastException

java.lang.String cannot be cast to java.lang.Object[]

1 android.util.ArrayMap.allocArrays(ArrayMap.java:174)
2 android.util.ArrayMap.put(ArrayMap.java:463)


不错这个问题在Android 9.0 修复了

<pre>
    public V removeAt(int index) {
        final Object old = mArray[(index << 1) + 1];
        final int osize = mSize;
        final int nsize;
        if (osize <= 1) {
            // Now empty.
            if (DEBUG) Log.d(TAG, "remove: shrink from " + mHashes.length + " to 0");
            
            //修改成下面那样
            //freeArrays(ohashes, oarray, osize);
            //mHashes = EmptyArray.INT;
            //mArray = EmptyArray.OBJECT;
            
            final int[] ohashes = mHashes;
            final Object[] oarray = mArray;
            mHashes = EmptyArray.INT;
            mArray = EmptyArray.OBJECT;
            freeArrays(ohashes, oarray, osize);
            ...
        }
        ...
    }       
</pre>

[参考](https://www.jianshu.com/p/02890898ee68)
[arraymap 原理](https://www.jianshu.com/p/1a14fc87b935)