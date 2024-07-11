     package MJShiftObject.Object;

     public class MJShiftObject {
       private int m_source_id;
       private int m_destination_id;
       private MJEShiftObjectType m_shift_type;
       private String m_source_character_name;

       public static MJShiftObject newInstance(ResultSet rs) throws SQLException {
         return newInstance()
           .set_source_id(rs.getInt("source_id"))
           .set_destination_id(rs.getInt("destination_id"))
           .set_shift_type(MJEShiftObjectType.from_name(rs.getString("shift_type")))
           .set_source_character_name(rs.getString("source_character_name"))
           .set_source_account_name(rs.getString("source_account_name"))
           .set_destination_character_name(rs.getString("destination_character_name"))
           .set_destination_account_name(rs.getString("destination_account_name"))
           .set_convert_parameters(rs.getString("convert_parameters"));
       }
       private String m_source_account_name; private String m_destination_character_name; private String m_destination_account_name; private String m_convert_parameters;
       public static MJShiftObject newInstance() {
         return new MJShiftObject();
       }











       public MJShiftObject set_source_id(int source_id) {
         this.m_source_id = source_id;
         return this;
       }
       public MJShiftObject set_destination_id(int destination_id) {
         this.m_destination_id = destination_id;
         return this;
       }
       public MJShiftObject set_shift_type(MJEShiftObjectType shift_type) {
         this.m_shift_type = shift_type;
         return this;
       }
       public MJShiftObject set_source_character_name(String source_character_name) {
         this.m_source_character_name = source_character_name;
         return this;
       }
       public MJShiftObject set_source_account_name(String source_account_name) {
         this.m_source_account_name = source_account_name;
         return this;
       }
       public MJShiftObject set_destination_character_name(String destination_character_name) {
         this.m_destination_character_name = destination_character_name;
         return this;
       }
       public MJShiftObject set_destination_account_name(String destination_account_name) {
         this.m_destination_account_name = destination_account_name;
         return this;
       }
       public MJShiftObject set_convert_parameters(String convert_parameters) {
         this.m_convert_parameters = convert_parameters;
         return this;
       }
       public int get_source_id() {
         return this.m_source_id;
       }
       public int get_destination_id() {
         return this.m_destination_id;
       }
       public MJEShiftObjectType get_shift_type() {
         return this.m_shift_type;
       }
       public String get_source_character_name() {
         return this.m_source_character_name;
       }
       public String get_source_account_name() {
         return this.m_source_account_name;
       }
       public String get_destination_character_name() {
         return this.m_destination_character_name;
       }
       public String get_destination_account_name() {
         return this.m_destination_account_name;
       }
       public String get_convert_parameters() {
         return this.m_convert_parameters;
       }
     }


