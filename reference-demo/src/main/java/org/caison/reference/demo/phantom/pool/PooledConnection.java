// Copyright (c) 2007 Keith D Gregory
package org.caison.reference.demo.phantom.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 包装JDBC数据连接，并使连接关联到连接池。
 * 关闭连接时将会返回包装好的连接放回池中为了下次复用。
 *
 * 这个类实现了InvocationHandler接口用于动态代理，这样做的原因有2个：
 * 1 大部分方法是简单的委托一个底层的连接实现逻辑。我们不必写一堆样板代码来实现Connection的系列继承接口
 * 2 更重要的，Connection接口会后续扩展升级，这样做可以更好向前后兼容，如果具体实现跟JDK版本绑定
 * (Connection接口位于JDK的rt.jar包中)，可能会导致这个示例代码活得不久
 *
 * 鉴于第2点，我肯定会在一个生产连接的池选择类似反射的实现，而且这个类中的反射方法相对不频繁地调用，
 * 因此反射的带来的额外开销并不是很重要
 */
public class PooledConnection implements InvocationHandler {
    private ConnectionPool pool;
    private Connection cxt;

    public PooledConnection(ConnectionPool pool, Connection cxt) {
        this.pool = pool;
        this.cxt = cxt;
    }

    private Connection getConnection() {
        try {
            if ((cxt == null) || cxt.isClosed()) {
                throw new RuntimeException("Connection is closed");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("unable to determine if underlying connection is open", ex);
        }

        return cxt;
    }

    /**
     *  基于代理创建连接
     */
    public static Connection newInstance(ConnectionPool pool, Connection cxt) {
        return (Connection) Proxy.newProxyInstance(PooledConnection.class.getClassLoader(),
                new Class[] { Connection.class }, new PooledConnection(pool, cxt));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            switch (method.getName()) {
            case "close":
                close();
                return null;
            case "isClosed":
                return isClosed();
            default:
                return method.invoke(getConnection(), args);
            }
        } catch (Throwable ex) {
            if (ex instanceof InvocationTargetException) {
                ex = ((InvocationTargetException) ex).getTargetException();
            }

            if ((ex instanceof Error) || (ex instanceof RuntimeException)
                    || (ex instanceof SQLException)) {
                throw ex;
            }

            // 反射调用会产生受检异常，需要包装成非受检异常
            throw new RuntimeException("exception during reflective invocation", ex);
        }
    }

    /**
     * Connection的方法不仅仅是简单的委托，还有一些池相关的操作
     * @throws SQLException
     */
    private void close() throws SQLException {
        if (cxt != null) {
            pool.releaseConnection(cxt);
            cxt = null;
        }
    }

    private boolean isClosed() throws SQLException {
        return (cxt == null) || (cxt.isClosed());
    }
}