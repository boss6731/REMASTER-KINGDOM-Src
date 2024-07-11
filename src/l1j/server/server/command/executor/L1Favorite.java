/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.command.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import l1j.server.server.GMCommands;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Favorite implements L1CommandExecutor {
	private static final Map<Integer, String> _faviCom = new HashMap<Integer, String>();

	private L1Favorite() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Favorite();
	}

	@Override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            // 檢查是否已經存在該角色的喜好命令，如果不存在則初始化
            if (!_faviCom.containsKey(pc.getId())) {
                _faviCom.put(pc.getId(), "");
            }
            String faviCom = _faviCom.get(pc.getId());

            // 如果命令以 "셋팅"（設置）開頭
            if (arg.startsWith("셋팅")) {
                // 註冊命令
                StringTokenizer st = new StringTokenizer(arg);
                st.nextToken(); // 跳過 "셋팅"

                // 如果沒有更多的令牌，發送錯誤消息
                if (!st.hasMoreTokens()) {
                    pc.sendPackets(new S_SystemMessage("沒有命令。"));
                    return;
                }

                StringBuilder cmd = new StringBuilder();
                String temp = st.nextToken(); // 獲取命令類型

                // 如果試圖註冊自身命令，發送錯誤消息
                if (temp.equalsIgnoreCase(cmdName)) {
                    pc.sendPackets(new S_SystemMessage(cmdName + " 無法註冊自身。"));
                    return;
                }

                cmd.append(temp + " ");
                // 拼接剩下的命令參數
                while (st.hasMoreTokens()) {
                    cmd.append(st.nextToken() + " ");
                }

                // 可以將 cmd.toString() 存儲到 faviCom 或進行其他操作
            }

            // 其他邏輯可以在此繼續...

        } catch (Exception e) {
            // 捕捉異常並發送錯誤消息給使用者
            pc.sendPackets(new S_SystemMessage("錯誤: 設置命令失敗"));
        }
    }
		// 註冊新命令
		faviCom = cmd.toString().trim();
		_faviCom.put(pc.getId(), faviCom);
		pc.sendPackets(new S_SystemMessage(faviCom + " 已註冊。"));

	} else if (arg.startsWith("查看")) { // 查看已註冊的命令
		pc.sendPackets(new S_SystemMessage("當前已註冊的命令: " + faviCom));


	} else if (faviCom.isEmpty()) { // 如果沒有已註冊的命令
		pc.sendPackets(new S_SystemMessage("沒有已註冊的命令。"));


	} else { // 執行已註冊的命令
		StringBuilder cmd = new StringBuilder();
			StringTokenizer st = new StringTokenizer(arg);
			StringTokenizer st2 = new StringTokenizer(faviCom);

			// 替換命令中的參數
			while (st2.hasMoreTokens()) {
				String temp = st2.nextToken();
				if (temp.startsWith("%")) {
					cmd.append(st.nextToken() + " ");
				} else {
					cmd.append(temp + " ");
				}
			}

			// 添加剩餘參數
			while (st.hasMoreTokens()) {
				cmd.append(st.nextToken() + " ");
			}

			pc.sendPackets(new S_SystemMessage(cmd + " 正在執行。"));
			GMCommands.getInstance().handleCommands(pc, cmd.toString());
	}


	} catch (Exception e) {
		// 捕捉異常並發送錯誤消息給使用者
			pc.sendPackets(new S_SystemMessage(cmdName + " 設置 [命令名稱] | " + cmdName + " 查看 | " + cmdName + " [參數] 請輸入。"));
			e.printStackTrace();
	}

}

