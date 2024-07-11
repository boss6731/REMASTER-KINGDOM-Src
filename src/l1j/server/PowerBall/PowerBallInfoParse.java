/**package l1j.server.PowerBall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.JSONException;
import org.json.JSONObject;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.utils.SQLUtil;


public class PowerBallInfoParse {
	private static PowerBallInfoParse _instance;
	public boolean game = false;
	
	public static PowerBallInfoParse getInstance() {
		if (_instance == null) {
			_instance = new PowerBallInfoParse();
		}
		return _instance;
	}
	
	public boolean PowerBallInfo() {
		try {
			// 엔트리 파워볼 정보
			JSONObject json = readJsonFromUrl("http://ntry.com/data/json/games/powerball/result.json");
			if (json == null) {
				System.out.println("파워볼 정보 리로딩(파워볼정보)");
				return false;
			}
			// 엔트리 파워볼 회차 && 분배율 정보(분배율은 구현안함)
			JSONObject infojson = readJsonFromUrl("http://ntry.com/data/json/games/dist.json");
			if (infojson == null) {
				System.out.println("파워볼 정보 리로딩(회차정보)");
				return false;
			}
			// 공유가 필요한 부분을 클래스를 하나만들어서 컨트롤러에 한번만 접근하도록 하면 락이 필요없다.
			PowerBallInfo info = new PowerBallInfo();
			if (json.get("date") != null && json.get("ball") != null && json.get("times") != null
					&& json.get("def_ball_sum") != null && json.get("def_ball_oe") != null
					&& json.get("def_ball_unover") != null && infojson.get("powerball") != null) {
				info.setDate((String) json.get("date"));
				info.setTotalNum(json.get("ball"));
				info.setNum(Integer.parseInt((String) json.get("times")));
				info.setPlusNum(Integer.parseInt((String) json.get("def_ball_sum")));
				info.setoddEven((String) json.get("def_ball_oe"));
				info.setUnderOver((String) json.get("def_ball_unover"));

				String num = "" + infojson.get("powerball") + "";
				JSONObject today = new JSONObject(num);
				info.setTodatCount((int) today.get("rd") - 1);
			}

			PowerBallController.getInstance().setinfo(info);

			if (!game) {
				CreatePowerBallInfo(info);
				PowerBallController.getInstance()._next = info.getNum() + 1;
				game = true;
			}

			if (info.getNum() != PowerBallController.getInstance()._next) {
				PowerBallController.getInstance()._next = info.getNum();
				PowerBallController.getInstance()._executeStatus = 3;
				PowerBallController.getInstance()._RemainTime = 10;
				PowerBallController.getInstance().setState(2);

				UpdatePowerBallInfo(info, info.getTodatCount());
				info.setNextNum(info.getNum() + 1);
				CreateNextBallInfo(info);
			}
			deleteId(info.getNum());
		} catch (java.net.MalformedURLException exception) {
			exception.printStackTrace();
			return false;
		} catch (java.io.IOException exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		BufferedReader rd = null;
		HttpURLConnection con = null;
		try {
			URL urlObject = new URL(url);
			con = (HttpURLConnection)urlObject.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
			con.connect();
			if(con.getResponseCode() != 200) {
				System.out.println(String.format("파워볼: ResponseCode %d %s", con.getResponseCode(), con.getResponseMessage()));
				return null;
			}
			
			//URLConnection con = urlObject.openConnection();
			InputStream is = con.getInputStream();
			rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			if(MJString.isNullOrEmpty(jsonText)) {
				return null;
			}
			JSONObject json = new JSONObject(jsonText);
			return json;
		}catch(JSONException e) {
			System.out.println("★JSON에서 오류가 발생됨★");
		}catch(UnknownHostException e) {
			System.out.println("파워볼: 호스트를 찾을 수 없습니다.");
		}catch(IOException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rd != null) {	
				try {
					rd.close();
				}catch(Exception e) {}
			}
			if(con != null) {
				try {
					con.disconnect();
				}catch(Exception e) {}
			}
		}
		return null;
	}
	
	public static void CreatePowerBallInfo(PowerBallInfo power) {
		Updator.exec(
				"insert ignore into Powerball_info set gamedate=?, num=?, TodayCount=?, plusnum=?, oddEven=?, unover=?, totalnum=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;					
						pstm.setString(++idx, "");
						pstm.setInt(++idx, power.getNum());
						pstm.setInt(++idx, power.getTodatCount());
						pstm.setInt(++idx, 0);
						pstm.setString(++idx, null);
						pstm.setString(++idx, null);
						pstm.setString(++idx, null);
					}
				});
	}
	
	public void UpdatePowerBallInfo(PowerBallInfo power, int num) {
		Updator.exec(
				"UPDATE Powerball_info SET gamedate=?, TodayCount=?, plusnum=?, oddEven=?, unover=?, totalnum=? WHERE TodayCount=" + num,
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
							pstm.setString(++idx, power.getDate());
							pstm.setInt(++idx, power.getTodatCount());
							pstm.setInt(++idx, power.getPlusNum());
							pstm.setString(++idx, power.getoddEven());
							pstm.setString(++idx, power.getUnderOver());
							pstm.setString(++idx, ""+power.getTotalNum()+"");
					}
				});
	}
	
	public static void CreateNextBallInfo(PowerBallInfo power) {
		Updator.exec(
				"insert ignore into Powerball_info set gamedate=?, num=?, TodayCount=?, plusnum=?, oddEven=? , unover=?, totalnum=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;					
						pstm.setString(++idx, null);
						pstm.setInt(++idx, power.getNextNum());
						pstm.setInt(++idx, power.getTodatCount()+1);
						pstm.setInt(++idx, 0);
						pstm.setString(++idx, null);
						pstm.setString(++idx, null);
						pstm.setString(++idx, null);
					}
				});
	}
	
	public void deleteId(int num) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM Powerball_info WHERE num <=?");
			pstm.setInt(1, num - 10);
			pstm.execute();
		} catch (Exception e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}**/