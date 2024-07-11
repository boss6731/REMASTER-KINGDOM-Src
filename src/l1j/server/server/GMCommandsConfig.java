     package l1j.server.server;

     import java.io.IOException;
     import java.util.ArrayList;
     import java.util.HashMap;
     import java.util.List;
     import java.util.logging.Level;
     import java.util.logging.Logger;
     import javax.xml.parsers.DocumentBuilder;
     import javax.xml.parsers.DocumentBuilderFactory;
     import javax.xml.parsers.ParserConfigurationException;
     import l1j.server.server.model.L1Location;
     import l1j.server.server.templates.L1ItemSetItem;
     import l1j.server.server.utils.IterableElementList;
     import org.w3c.dom.Document;
     import org.w3c.dom.Element;
     import org.w3c.dom.NodeList;
     import org.xml.sax.SAXException;





     public class GMCommandsConfig
     {
       private static Logger _log = Logger.getLogger(GMCommandsConfig.class
           .getName());

       private static interface ConfigLoader {
         void load(Element param1Element);
       }

       private abstract class ListLoaderAdapter implements ConfigLoader {
         private final String _listName;

         public ListLoaderAdapter(String listName) {
           this._listName = listName;
         }


         public final void load(Element element) {
           NodeList nodes = element.getChildNodes();
           for (Element elem : new IterableElementList(nodes)) {
             if (elem.getNodeName().equalsIgnoreCase(this._listName))
               loadElement(elem);
           }
         }

         public abstract void loadElement(Element param1Element);
       }

       private class RoomLoader
         extends ListLoaderAdapter {
         public RoomLoader() {
           super("Room");
         }


         public void loadElement(Element element) {
           String name = element.getAttribute("Name");
           int locX = Integer.valueOf(element.getAttribute("LocX")).intValue();
           int locY = Integer.valueOf(element.getAttribute("LocY")).intValue();
           int mapId = Integer.valueOf(element.getAttribute("MapId")).intValue();
           GMCommandsConfig.ROOMS.put(name.toLowerCase(), new L1Location(locX, locY, mapId));
         }
       }

       private class ItemSetLoader extends ListLoaderAdapter {
         public ItemSetLoader() {
           super("ItemSet");
         }

         public L1ItemSetItem loadItem(Element element) {
           int id = Integer.valueOf(element.getAttribute("Id")).intValue();
           int amount = Integer.valueOf(element.getAttribute("Amount")).intValue();
           int enchant = Integer.valueOf(element.getAttribute("Enchant")).intValue();
           return new L1ItemSetItem(id, amount, enchant);
         }


         public void loadElement(Element element) {
           List<L1ItemSetItem> list = new ArrayList<>();
           NodeList nodes = element.getChildNodes();
           for (Element elem : new IterableElementList(nodes)) {
             if (elem.getNodeName().equalsIgnoreCase("Item")) {
               list.add(loadItem(elem));
             }
           }
           String name = element.getAttribute("Name");
           GMCommandsConfig.ITEM_SETS.put(name.toLowerCase(), list);
         }
       }

       private static HashMap<String, ConfigLoader> _loaders = new HashMap<>();
       static {
         GMCommandsConfig instance = new GMCommandsConfig();
         instance.getClass(); _loaders.put("roomlist", new RoomLoader());
         instance.getClass(); _loaders.put("itemsetlist", new ItemSetLoader());
       }

       public static HashMap<String, L1Location> ROOMS = new HashMap<>();
       public static HashMap<String, List<L1ItemSetItem>> ITEM_SETS = new HashMap<>();



       private static Document loadXml(String file) throws ParserConfigurationException, SAXException, IOException {
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         return builder.parse(file);
       }

         public static void load() {
             try {
                 // 加載指定路徑的 XML 文件
                 Document doc = loadXml("./data/xml/GmCommands/GMCommands.xml");

                 // 獲取根元素的所有子節點
                 NodeList nodes = doc.getDocumentElement().getChildNodes();

                 // 初始化 ConfigLoader 變數
                 ConfigLoader loader = null;

                 // 遍歷所有子節點
                 for (int i = 0; i < nodes.getLength(); i++) {
                     // 根據節點名稱獲取對應的 ConfigLoader
                     loader = _loaders.get(nodes.item(i).getNodeName().toLowerCase());
                     if (loader != null) {
                         // 調用 load 方法加載配置
                         loader.load((Element) nodes.item(i));
                     }
                 }
             } catch (Exception e) {
                 // 捕獲並記錄異常
                 _log.log(Level.SEVERE, "GMCommands.xml 的讀取失敗", e);
             }
         }


