package com.example.todo.core;

import java.sql.Connection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ProxyMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        
        Object obj = invocation.proceed();
        if (obj instanceof Connection) {
            return new ProxyConnection((Connection) obj);
        }
        return obj;
    }

}
