 package l1j.server.server.utils;

 import java.util.Iterator;
 import java.util.NoSuchElementException;
 import org.w3c.dom.Node;
 import org.w3c.dom.NodeList;


 public class IterableNodeList
   implements Iterable<Node>
 {
   private final NodeList _list;

   private class MyIterator
     implements Iterator<Node>
   {
     private int _idx = 0;


     public boolean hasNext() {
       return (this._idx < IterableNodeList.this._list.getLength());
     }


     public Node next() {
       if (!hasNext()) {
         throw new NoSuchElementException();
       }
       return IterableNodeList.this._list.item(this._idx++);
     }
     private MyIterator() {}

     public void remove() {
       throw new UnsupportedOperationException();
     }
   }

   public IterableNodeList(NodeList list) {
     this._list = list;
   }


   public Iterator<Node> iterator() {
     return new MyIterator();
   }
 }


