// Copyright (c) 2007 Keith D Gregory
package org.caison.reference.demo.phantom.pool;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * 使用内存中的HSQL数据库演示连接池，和一个请求连接的主线程，让连接不显式关闭。
 * 当连接返回时，池将记录。
 */
public class ConnectionPoolDemo {
    public static void main(String[] argv) throws Exception {
        ConnectionPool pool = new ConnectionPool("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:aname",
                "sa", "", 5);

        Connection cxt = null;
        for (int i = 0; i < 10; i++) {
            // 这个赋值会导致之前获取的连接没有被用户释放，这些连接可以被回收
            // 我们获取元数据以验证连接是否有效
            cxt = pool.getConnection();
            cxt.getMetaData();
            attemptGC();
        }
    }

    /**
     * 尝试调用gc
     */
    private static void attemptGC() {
        // 根据我的经验，调用System.gc（）是不够的; 分配内存块使它实际上做了一些工作
        ArrayList<byte[]> foo = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            foo.add(new byte[1024]);
        }
        System.gc();
    }
}