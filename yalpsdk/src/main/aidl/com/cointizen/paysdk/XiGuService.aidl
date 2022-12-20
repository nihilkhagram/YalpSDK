// XiGuService.aidl
package com.cointizen.paysdk;

// Declare any non-default types here with import statements

interface XiGuService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void sendMsg2Baidu(int type, String msg, String serviceName);
}