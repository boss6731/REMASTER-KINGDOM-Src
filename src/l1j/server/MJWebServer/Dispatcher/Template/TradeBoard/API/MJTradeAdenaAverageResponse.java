package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.gson.JsonObject;

import MJNCoinSystem.MJNCoinSettings;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJTradeAdenaAverageResponse extends MJHttpResponse {
	
	private boolean is_text;
	
	public MJTradeAdenaAverageResponse(MJHttpRequest request) {
		super(request);
		
		String url_number = request.get_request_uri().replaceAll("/api/trade/adena/average/", "");
		if(url_number.equalsIgnoreCase("text")) {
			is_text = true;
		} else {
			is_text = false;
		}
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder stringBuilder = new StringBuilder();
		
		HttpResponse response = null;
		if(is_text) {
			getAdenaAverageInfo(stringBuilder);
			response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		} else {
			JsonObject json = new JsonObject();
			getAdenaAverageInfoInt(json);
			
			response = create_response(HttpResponseStatus.OK, json.toString());
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		}
		
		return response;
	}

	private void getAdenaAverageInfoInt(JsonObject json) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int total_item_count = 0;
		int total_price = 0;
		int result_price = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE category=?");
			pstm.setString(1, "金幣");
			rs = pstm.executeQuery();

			while (rs.next()) {
				if(rs.getTimestamp("buy_result_date") != null) {
					total_item_count += rs.getInt("item_count") / MJNCoinSettings.ADENA_GENERATE_UNIT;
					total_price += rs.getInt("price");
				}
			}
// TODO: 修改物品交易中阿德納銷售金額的表示和限制部分
			if(total_price != 0 && total_item_count > 0) { // 註釋
				result_price = total_price / total_item_count;
				json.addProperty("average", result_price);
			} else {
				result_price = MJNCoinSettings.ADENA_MARKET_PRICE;
				json.addProperty("average", result_price);
			}
// TODO: 實時顯示阿德納的售價
			json.addProperty("min_price", MJNCoinSettings.ADENA_MARKET_PRICE);
			json.addProperty("min_average", result_price);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}


	private void getAdenaAverageInfo(StringBuilder stringBuilder) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int total_item_count = 0;
		int total_price = 0;
		int result_price = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE category=?");
			pstm.setString(1, "金幣");
			rs = pstm.executeQuery();

			while (rs.next()) {
				if (rs.getTimestamp("buy_result_date") != null) {
					total_item_count += rs.getInt("item_count") / MJNCoinSettings.ADENA_GENERATE_UNIT;
					total_price += rs.getInt("price");
				}
			}

			if(total_price != 0 && total_item_count > 0) {
				result_price = total_price / total_item_count;
				if(result_price <= MJNCoinSettings.ADENA_MARKET_PRICE) {
					result_price = MJNCoinSettings.ADENA_MARKET_PRICE;
				}
			} else {
				result_price = MJNCoinSettings.ADENA_MARKET_PRICE;
			}

			stringBuilder.append("<span class="exp_txt"> * 實時平均銷售價格統計 <font style="color:#da2121;"> " + MJNCoinSettings.ADENA_MARKET_STATISTICS + "千萬 <font style="color:#9d9692;">金幣當 <font style="color:#da2121;">");
			stringBuilder.append(MJString.parse_money_string(result_price) + "元");
// stringBuilder.append("<span class="exp_txt"> * 實時管理員購買統計 <font style="color:#da2121;"> " + MJNCoinSettings.GM_Purchase_Count + "億 <font style="color:#9d9692;">阿德納當 <font style="color:#da2121;">");
// stringBuilder.append(MJString.parse_money_string(MJNCoinSettings.GM_Cash_Count) + "元");
			stringBuilder.append("<span class="exp_txt"> * 輸入帳戶密碼時<font style="color:#da2121;"> [英文]<font style="color:#9d9692;">切換後輸入。<font style="color:#da2121;">");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
