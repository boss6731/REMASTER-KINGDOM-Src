 package l1j.server.server.datatables;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

 public class AlchemyProbability {
   private static AlchemyProbability _instance;
   private HashMap<Integer, AlchemyProbStruct> _alchemy_structs;

   public static AlchemyProbability getInstance() {
     if (_instance == null)
       _instance = new AlchemyProbability();
     return _instance;
   }

   public static void release() {
     if (_instance != null) {
       _instance.dispose();
       _instance = null;
     }
   }

   public static void reload() {
     AlchemyProbability old = _instance;
     _instance = new AlchemyProbability();
     if (old != null) {
       old.dispose();
       old = null;
     }
   }


   private AlchemyProbability() {
     this._alchemy_structs = load();
   }

   private HashMap<Integer, AlchemyProbStruct> load() {
     final HashMap<Integer, AlchemyProbStruct> sequences = new HashMap<>(8);
     Selector.exec("select * from tb_alchemy_probability", (SelectorHandler)new FullSelectorHandler()
         {
           public void result(ResultSet rs) throws Exception {
             while (rs.next()) {
               AlchemyProbability.AlchemyProbStruct struct = AlchemyProbability.AlchemyProbStruct.newInstance(rs);
               sequences.put(Integer.valueOf(struct.get_alchemy_id()), struct);
             }
           }
         });
     return sequences;
   }

   public Integer select_id(int alchemy_id) {
     return Integer.valueOf(MJAlchemyProbabilityBox.getInstance().nextCurrentAlchemyId(alchemy_id));
   }


   public boolean is_winning(int alchemy_id, int amount) {
     return ((AlchemyProbStruct)this._alchemy_structs.get(Integer.valueOf(alchemy_id))).is_winning(amount);
   }

   public void dispose() {
     if (this._alchemy_structs != null) {
       this._alchemy_structs.clear();
       this._alchemy_structs = null;
     }
   }
   public static class AlchemyProbStruct { private int _alchemy_id;

     public static AlchemyProbStruct newInstance(ResultSet rs) throws SQLException {
       return newInstance()
         .set_alchemy_id(rs.getInt("alchemy_id"))
         .set_probability_by_millions((rs.getString("success_probability_by_millions") == null) ? null : (Integer[])MJArrangeParser.parsing(rs.getString("success_probability_by_millions"), ",", MJArrangeParseeFactory.createIntArrange()).result())
         .set_sequences((Integer[])MJArrangeParser.parsing(rs.getString("sequence"), ",", MJArrangeParseeFactory.createIntArrange()).result());
     }
     private Integer[] _probability_by_millions; private Integer[] _sequences;
     public static AlchemyProbStruct newInstance() {
       return new AlchemyProbStruct();
     }





     public AlchemyProbStruct set_alchemy_id(int alchemy_id) {
       this._alchemy_id = alchemy_id;
       return this;
     }

     public int get_alchemy_id() {
       return this._alchemy_id;
     }

     public AlchemyProbStruct set_probability_by_millions(Integer[] probability_by_millions) {
       this._probability_by_millions = probability_by_millions;
       return this;
     }

     public Integer[] get_probability_by_millions() {
       return this._probability_by_millions;
     }

     public AlchemyProbStruct set_sequences(Integer[] sequences) {
       this._sequences = sequences;
       return this;
     }

     public Integer[] get_sequences() {
       return this._sequences;
     }

     public Integer select_id() {
       for (Integer id : this._sequences) {
         if (MJRnd.isBoolean()) {
           return id;
         }
       }
       return this._sequences[MJRnd.next(this._sequences.length)];
     }

     public boolean is_winning(int amount) {
       return MJRnd.isWinning(1000000, this._probability_by_millions[amount - 1].intValue());
     } }

 }


