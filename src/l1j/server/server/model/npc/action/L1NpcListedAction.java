 package l1j.server.server.model.npc.action;

 import java.util.List;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.npc.L1NpcHtml;
 import org.w3c.dom.Element;



















 public class L1NpcListedAction
   extends L1NpcXmlAction
 {
   private List<L1NpcAction> _actions;

   public L1NpcListedAction(Element element) {
     super(element);
     this._actions = L1NpcXmlParser.listActions(element);
   }


   public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
     L1NpcHtml result = null;
     L1NpcHtml r = null;
     for (L1NpcAction action : this._actions) {
       if (!action.acceptsRequest(actionName, pc, obj)) {
         continue;
       }
       r = action.execute(actionName, pc, obj, args);
       if (r != null) {
         result = r;
       }
     }
     return result;
   }


   public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, int amount) {
     L1NpcHtml result = null;
     L1NpcHtml r = null;
     for (L1NpcAction action : this._actions) {
       if (!action.acceptsRequest(actionName, pc, obj)) {
         continue;
       }
       r = action.executeWithAmount(actionName, pc, obj, amount);
       if (r != null) {
         result = r;
       }
     }
     return result;
   }
 }


