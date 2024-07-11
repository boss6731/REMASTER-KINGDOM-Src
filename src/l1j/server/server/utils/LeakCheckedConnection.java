     package l1j.server.server.utils;

     import java.lang.reflect.InvocationHandler;
     import java.lang.reflect.InvocationTargetException;
     import java.lang.reflect.Method;
     import java.lang.reflect.Proxy;
     import java.sql.Connection;
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.sql.Statement;
     import java.util.HashMap;
     import java.util.Map;
     import java.util.logging.Level;
     import java.util.logging.Logger;



     public class LeakCheckedConnection
     {
       private static final Logger _log = Logger.getLogger(LeakCheckedConnection.class.getName());
       private Connection _con;
       private Throwable _stackTrace;
       private Map<Statement, Throwable> _openedStatements = new HashMap<>();
       private Map<ResultSet, Throwable> _openedResultSets = new HashMap<>();
       private Object _proxy;

       private LeakCheckedConnection(Connection con) {
         this._con = con;
         this._proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, new ConnectionHandler());

         set_stackTrace(new Throwable());
       }

       public static Connection create(Connection con) {
         return (Connection)(new LeakCheckedConnection(con))._proxy;
       }

       private Object send(Object o, Method m, Object[] args) throws Throwable {
         try {
           return m.invoke(o, args);
         } catch (InvocationTargetException e) {
           if (e.getCause() != null) {
             throw e.getCause();
           }
           throw e;
         }
       }

       private void remove(Object o) {
         if (o instanceof ResultSet) {
           this._openedResultSets.remove(o);
         } else if (o instanceof Statement) {
           this._openedStatements.remove(o);
         } else {
           throw new IllegalArgumentException("bad class:" + o);
         }
       }

       void closeAll() {
         if (!this._openedResultSets.isEmpty()) {
           for (Throwable t : this._openedResultSets.values()) {
             _log.log(Level.WARNING, "Leaked ResultSets detected.", t);
           }
         }
         if (!this._openedStatements.isEmpty()) {
           for (Throwable t : this._openedStatements.values()) {
             _log.log(Level.WARNING, "Leaked Statement detected.", t);
           }
         }
         for (ResultSet rs : this._openedResultSets.keySet()) {
           SQLUtil.close(rs);
         }
         for (Statement ps : this._openedStatements.keySet()) {
           SQLUtil.close(ps);
         }
       }

       public void set_stackTrace(Throwable _stackTrace) {
         this._stackTrace = _stackTrace;
       }

       public Throwable get_stackTrace() {
         return this._stackTrace;
       }

       private class ConnectionHandler
         implements InvocationHandler {
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           if (method.getName().equals("close")) {
             LeakCheckedConnection.this.closeAll();
           }
           Object o = LeakCheckedConnection.this.send(LeakCheckedConnection.this._con, method, args);
           if (o instanceof Statement) {
             LeakCheckedConnection.this._openedStatements.put((Statement)o, new Throwable());
             o = (new LeakCheckedConnection.Delegate(o, PreparedStatement.class))._delegateProxy;
           }
           return o;
         }

         private ConnectionHandler() {} }

       private class Delegate implements InvocationHandler { private Object _delegateProxy;
         private Object _original;

         Delegate(Object o, Class<?> c) {
           this._original = o;
           this._delegateProxy = Proxy.newProxyInstance(c.getClassLoader(), new Class[] { c }, this);
         }

         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           if (method.getName().equals("close")) {
             LeakCheckedConnection.this.remove(this._original);
           }
           Object o = LeakCheckedConnection.this.send(this._original, method, args);
           if (o instanceof ResultSet) {
             LeakCheckedConnection.this._openedResultSets.put((ResultSet)o, new Throwable());
             o = (new Delegate(o, ResultSet.class))._delegateProxy;
           }
           return o;
         } }

     }


