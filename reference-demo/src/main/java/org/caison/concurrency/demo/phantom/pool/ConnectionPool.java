// Copyright (c) 2007 Keith D Gregory
package org.caison.reference.demo.phantom.pool;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * 管理数据库连接池，如果调用者忘记，使用幻像引用将连接返回到池。
 * 为了正常工作，我们使用{@link PooledConnection}，它包装实际连接并委派大多数操作。
 * 用户被赋予此对象，我们维护一个幻像引用，以及对实际连接的强引用。
 * 当幻像进入引用队列回收时，我们返回到池的实际连接。
 *
 * 由于这是演示代码，因此我们在构造池时创建最大连接数，并且不尝试重新生成关闭的连接。
 */
public class ConnectionPool {
    private Queue<Connection> poolQueue = new LinkedList<>();

    /**
     * 用于识别已经被回收的连接
     */
    private ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
    /**
     * IdentityHashMap与HashMap的不同之处在于：HashMap使用equals判断2个key是否相同，IdentityHashMap使用==判断2个key是否相同
     * 连接与引用对象之间双向映射，在返回连接池时使用
     */
    private IdentityHashMap<Object, Connection> ref2Cxt = new IdentityHashMap<>();
    private IdentityHashMap<Connection, Object> cxt2Ref = new IdentityHashMap<>();

    /**
     *  @param  driver  jdbc驱动类用于连接T
     *  @param  url     数据库URL地址
     *  @param  user    数据库用户名
     *  @param  password  数据库密码
     *  @param  maxConn 连接池运行的最大连接数
     *
     *  @throws RuntimeException 构建失败的运行异常
     */
    public ConnectionPool(String driver, String url, String user, String password, int maxConn) {
        try {
            Class.forName(driver);
            for (int ii = 0; ii < maxConn; ii++) {
                poolQueue.add(DriverManager.getConnection(url, user, password));
            }
        } catch (Exception e) {
            throw new RuntimeException("unable to initialize", e);
        }
    }

    /**
     * 这个内部方法用于从池中检索连接，将其与虚引用相关联。
     * 这是从{@link #getConnection}调用的，它负责确保池中有连接。
     * @param cxt  待包装的连接
     * @return 包装后的连接
     */
    private synchronized Connection wrapConnection(Connection cxt) {
        Connection wrapped = PooledConnection.newInstance(this, cxt);
        PhantomReference<Connection> ref = new PhantomReference<>(wrapped, refQueue);
        cxt2Ref.put(cxt, ref);
        ref2Cxt.put(ref, cxt);
        System.err.println("Acquired connection " + cxt);
        return wrapped;
    }

    /**
     * 释放一个连接到池中，这个方法主要用于被{@link PooledConnection}调用，
     * 没有别的地方会调用，使用作用域是protected
     * @param cxt 连接
     */
    synchronized void releaseConnection(Connection cxt) {
        Object ref = cxt2Ref.remove(cxt);
        ref2Cxt.remove(ref);
        poolQueue.offer(cxt);
        System.err.println("Released connection " + cxt);
    }

    /**
     * 当引用对象进入引用队列，释放连接
     * @param ref 引用对象
     */
    private synchronized void releaseConnection(Reference<?> ref) {
        Connection cxt = ref2Cxt.remove(ref);
        if (cxt != null) {
            releaseConnection(cxt);
        }
    }

    /**
     * 当连接池中没有可用连接时被{@link #getConnection}方法调用，用于检测是否有连接被垃圾收集器释放。
     * 此函数等待很短的时间，但随后返回，以便调用者可以再次查看池。
     */
    private void tryWaitingForGarbageCollector() {
        try {
            Reference<?> ref = refQueue.remove(100);
            if (ref != null) {
                releaseConnection(ref);
            }
        } catch (InterruptedException ignored) {
            // we have to catch this exception, but it provides no information here
            // a production-quality pool might use it as part of an orderly shutdown
        }
    }

    /**
     * 从连接池中检索获取一个可用连接，方法会阻塞直到有可用连接
     * @return 连接
     */
    public Connection getConnection() {
        while (true) {
            synchronized (this) {
                if (poolQueue.size() > 0) {
                    return wrapConnection(poolQueue.remove());
                }
            }
            tryWaitingForGarbageCollector();
        }
    }
}