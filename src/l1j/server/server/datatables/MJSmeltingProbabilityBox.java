 package l1j.server.server.datatables;
 import java.io.BufferedInputStream;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.UnsupportedEncodingException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collections;
 import l1j.server.MJTemplate.Command.MJCommand;
 import l1j.server.MJTemplate.Command.MJCommandArgs;
 import l1j.server.MJTemplate.Command.MJCommandTree;

 public class MJSmeltingProbabilityBox implements MJCommand {
   private static MJSmeltingProbabilityBox _instance;
   private ArrayList<ArrayList<Integer>> m_probability_boxes;

   public static MJSmeltingProbabilityBox getInstance() {
     if (_instance == null)
       _instance = new MJSmeltingProbabilityBox();
     return _instance;
   }
   private int[] m_boxes_index; private MJCommandTree _commands;
   public static void release() {
     if (_instance != null) {
       _instance.dispose();
       _instance = null;
     }
   }

   public static void reload() {
     MJSmeltingProbabilityBox old = _instance;
     _instance = new MJSmeltingProbabilityBox();
     if (old != null) {
       old.dispose();
       old = null;
     }
   }




   private MJSmeltingProbabilityBox() {
     initialize();
   }

   public void shuffleList(int alchemyId) {
     ArrayList<Integer> list = this.m_probability_boxes.get(alchemyId - 1);
     ArrayList<Integer> new_list = new ArrayList<>(list);
     Collections.shuffle(new_list);
     this.m_probability_boxes.set(alchemyId - 1, new_list);
   }

   public void shuffleList() {
     try {
       for (int i = 1; i <= this.m_probability_boxes.size(); i++)
         shuffleList(i);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public void initialize_index(int alchemyId) {
     this.m_boxes_index[alchemyId - 1] = 0;
   }

   public void initialize_index() {
     for (int i = 1; i <= this.m_probability_boxes.size(); i++) {
       initialize_index(i);
     }
   }



   public int nextCurrentAlchemyId(int alchemyId) {
     int index = this.m_boxes_index[alchemyId - 1];
     ArrayList<Integer> list = this.m_probability_boxes.get(alchemyId - 1);
     int result = ((Integer)list.get(index % list.size())).intValue();
     this.m_boxes_index[alchemyId - 1] = (index >= list.size()) ? 0 : (index + 1);
     return result;
   }

   private void initialize() {
     this._commands = createCommand();
     this.m_probability_boxes = new ArrayList<>();

     int idx = 1;
     while (true) {
       String path = String.format("config/ASmeltingBox%d.txt", new Object[] { Integer.valueOf(idx) });
       File f = new File(path);
       if (!f.exists()) {
         break;
       }
       try {
         ArrayList<Integer> list = loadProbabilityBox(idx);
         this.m_probability_boxes.add(list);
       } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
       }
       idx++;
     }
     this.m_boxes_index = new int[this.m_probability_boxes.size()];
     Arrays.fill(this.m_boxes_index, 0);
   }

     private ArrayList<Integer> loadProbabilityBox(int alchemyLevel) throws UnsupportedEncodingException {
// 建立一個根據煉金等級來設定檔案路徑的方法
         String path = String.format("config/ASmeltingBox%d.txt", new Object[] { Integer.valueOf(alchemyLevel) });
         byte[] buff = readFile(path);
         if (buff == null) {
// 如果檔案不存在或讀取出現問題，則打印錯誤訊息
             System.out.println(String.format("文件 %s 不存在或讀取時出現問題.", new Object[] { path }));
// System.out.println(String.format("文件 %s 不存在或讀取時出現問題。", new Object[] { path }));
             return null;
         }

// 使用MS949編碼將讀取到的字節數組轉換成字符串
         String boxesMessage = new String(buff, "UTF-8");
// 以換行符分割字符串，得到各個條目
         String[] boxesInfo = boxesMessage.split("\r\n");
         int count = boxesInfo.length;
// 建立一個整數型ArrayList，用於儲存條目
         ArrayList<Integer> list = new ArrayList<>(count);
         for (int i = 0; i < count; i++) {

             String info = boxesInfo[i].trim().replace(",", "");
// 檢查字符串是否為空或以"#"開始
             if (!isNullOrEmpty(info) && !info.startsWith("#"))

                 try {
// 嘗試將字符串轉換為整數並添加到列表中
                     list.add(Integer.valueOf(Integer.parseInt(info)));
                 } catch (Exception e) {
// 如果轉換失敗，打印錯誤訊息和堆棧追蹤
                     System.out.println(String.format("在文件 %s 中發現無法讀取的數據，將跳過. => %s", new Object[] { path, info }));
// System.out.println(String.format("在文件 %s 中發現無法讀取的數據，將跳過。 => %s", new Object[] { path, info }));
                     e.printStackTrace();
                 }
         }
         return list;
     }
     if (list == null || list.size() <= 0) {
       return null;
     }
     return list;
   }

   private static boolean isNullOrEmpty(String s) {
     return (s == null || s.equals(""));
   }

   private byte[] readFile(String path) {
     FileInputStream fs = null;
     BufferedInputStream is = null;
     byte[] buff = null;
     try {
       fs = new FileInputStream(path);
       is = new BufferedInputStream(fs);
       buff = new byte[(int)fs.getChannel().size()];
       is.read(buff, 0, buff.length);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       if (fs != null) {
         try {
           fs.close();
           fs = null;
         } catch (Exception exception) {}
       }

       if (is != null) {
         try {
           is.close();
           is = null;
         } catch (Exception exception) {}
       }
     }
     return buff;
   }

   public void dispose() {
     if (this.m_probability_boxes != null) {
       this.m_probability_boxes = null;
     }
   }


   public void execute(MJCommandArgs args) {
     this._commands.execute(args, (new StringBuilder(256)).append(this._commands.to_operation()));
   }

     private MJCommandTree createCommand() {
            // 建立新的命令樹，根命令為 ".재련석박스" (精鍊石箱)，包含子命令 "[리로드][인덱스초기화][섞기]"
         return (new MJCommandTree(".精鍊石箱", "[重新載入][索引初始化][隨機播放]", null))
                 .add_command(new MJCommandTree("重新載入", "", null)
                 {
                     // 當"리로드"（重新載入）命令被處理時執行此方法
                     protected void to_handle_command(MJCommandArgs args) throws Exception {
                         MJSmeltingProbabilityBox.reload();
                        // 通知使用者"精鍊石概率箱已重新載入"
                         args.notify("精鍊石概率箱已重新載入。");
                        // args.notify("精鍊石概率箱已重新載入。");
                     }
                 })
                 .add_command(new MJCommandTree("索引初始化", "", null)
                 {
                     // 當"인덱스초기화"（索引初始化）命令被處理時執行此方法
                     protected void to_handle_command(MJCommandArgs args) throws Exception {
                         MJSmeltingProbabilityBox.this.initialize_index();
                            // 通知使用者"精鍊石概率箱索引已初始化"
                         args.notify("精鍊石概率箱索引已初始化。");
                        // args.notify("精鍊石概率箱索引已初始化。");
                     }
                 })
                 .add_command(new MJCommandTree("混合", "", null)
                 {
                     // 當"섞기"（混合）命令被處理時執行此方法
                     protected void to_handle_command(MJCommandArgs args) throws Exception {
                         MJSmeltingProbabilityBox.this.shuffleList();
                            // 通知使用者"精鍊石概率箱已混合"
                         args.notify("精鍊石概率箱已混合。");
                        // args.notify("精鍊石概率箱已混合。");
                     }
                 });
     }


