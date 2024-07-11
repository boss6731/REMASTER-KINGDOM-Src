 package l1j.server.server.model.npc.action;

 import java.lang.reflect.Constructor;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import org.w3c.dom.Element;


 public class L1NpcActionFactory
 {
   private static Logger _log = Logger.getLogger(L1NpcActionFactory.class.getName());
   private static Map<String, Constructor<L1NpcAction>> _actions = new HashMap<>();


     // 定義靜態方法加載構造函數
     private static Constructor<L1NpcAction> loadConstructor(Class<L1NpcAction> c) throws NoSuchMethodException {
            // 獲取指定類的構造函數，參數為 Element 類型
         return c.getConstructor(new Class[] { Element.class });
     }

     // 靜態代碼塊，用於初始化 _actions 映射
     static {
         try {
        // 將不同的 NPC 動作類及其構造函數放入 _actions 映射中
             _actions.put("Action", loadConstructor(L1NpcListedAction.class));
             _actions.put("MakeItem", loadConstructor(L1NpcMakeItemAction.class));
             _actions.put("ShowHtml", loadConstructor(L1NpcShowHtmlAction.class));
             _actions.put("SetQuest", loadConstructor(L1NpcSetQuestAction.class));
             _actions.put("Teleport", loadConstructor(L1NpcTeleportAction.class));
         } catch (NoSuchMethodException e) {
            // 如果獲取構造函數失敗，記錄嚴重級別的日誌
             _log.log(Level.SEVERE, "加載 NpcAction 類失敗", e);
         }
     }

     // 定義靜態方法創建新的 NPC 動作
     public static L1NpcAction newAction(Element element) {
         try {
        // 根據元素名稱獲取對應的構造函數

             Constructor<L1NpcAction> con = _actions.get(element.getNodeName());
        // 使用構造函數創建新的 NPC 動作實例
             return con.newInstance(new Object[] { element });
         } catch (NullPointerException e) {
        // 如果沒有對應的構造函數，記錄警告級別的日誌並打印提示信息
             _log.warning(element.getNodeName() + " 未定義的 NPC 動作");
             System.out.println(element.getNodeName() + " 未定義的 NPC 動作");
         } catch (Exception e) {
        // 如果創建實例失敗，記錄嚴重級別的日誌並打印元素名稱
             _log.log(Level.SEVERE, "加載 NpcAction 類失敗", e);
             System.out.println(element.getNodeName());
         }
        // 返回 null 表示創建失敗
         return null;
     }


