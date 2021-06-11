package com.es.phoneshop.model.product.lock;

import javax.servlet.http.HttpSession;

public class SessionLock {
    private static final Object LOCK = new Object();

    public static Object getSessionLock(HttpSession session, String lockName) {
        if (lockName == null) lockName = "SESSION_LOCK";
        Object result = session.getAttribute(lockName);
        if (result == null) {
            synchronized (LOCK) {
                result = session.getAttribute(lockName);
                if (result == null) {
                    result = new Object();
                    session.setAttribute(lockName, result);
                }
            }
        }
        return result;
    }
}
