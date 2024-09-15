package l1j.server.server.server.datatables;

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

public abstract class MJSmeltingProbabilityBox implements MJCommand {
	private static MJSmeltingProbabilityBox _instance = new MJSmeltingProbabilityBox() {
        @Override
        public void execute(MJCommandArgs args) {

        }
    };

	public static MJSmeltingProbabilityBox getInstance() {
		if (_instance == null)
			_instance = new MyMJSmeltingProbabilityBox();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		MJSmeltingProbabilityBox old = _instance;
		_instance = new MJSmeltingProbabilityBox() {
			@Override
			public void execute(MJCommandArgs args) {

			}
		};
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	private ArrayList<ArrayList<Integer>> m_probability_boxes;
	private int[] m_boxes_index;
	private MJCommandTree _commands;

	private MJSmeltingProbabilityBox() {
		initialize();
	}

	public void shuffleList(int alchemyId) {
		ArrayList<Integer> list = m_probability_boxes.get(alchemyId - 1);
		ArrayList<Integer> new_list = new ArrayList<Integer>(list);
		Collections.shuffle(new_list);
		m_probability_boxes.set(alchemyId - 1, new_list);
	}

	public void shuffleList() {
		try {
			for (int i = 1; i <= m_probability_boxes.size(); ++i)
				shuffleList(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initialize_index(int alchemyId) {
		m_boxes_index[alchemyId - 1] = 0;
	}

	public void initialize_index() {
		for (int i = 1; i <= m_probability_boxes.size(); ++i)
			initialize_index(i);
	}

	// ÙíâÍÎ¦ÝÂÚÂÊïò¢òäú¼ÔÒÜÆ£¬ì×ó®ÜôÓ¤Ô¼òäú¼ÔÒÜÆðÃíÂ¡£
	// ?øµõóúÞñìÜÜâ¦Ëà£¬Í§÷Öå¥Òö?Ìî?ëëÓß¡£
	public int nextCurrentAlchemyId(int alchemyId) {
//		System.out.println(alchemyId);
		int index = m_boxes_index[alchemyId - 1];
		ArrayList<Integer> list = m_probability_boxes.get(alchemyId - 1);
		int result = list.get(index % list.size());
		m_boxes_index[alchemyId - 1] = index >= list.size() ? 0 : index + 1;
		return result;
	}

	private void initialize() {
		_commands = createCommand();
		m_probability_boxes = new ArrayList<ArrayList<Integer>>();

		int idx = 1;
		while (true) {
			String path = String.format("config/ASmeltingBox%d.txt", idx);
			File f = new File(path);
			if (!f.exists())
				break;

			try {
				ArrayList<Integer> list = loadProbabilityBox(idx);
				m_probability_boxes.add(list);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			++idx;
		}
		m_boxes_index = new int[m_probability_boxes.size()];
		Arrays.fill(m_boxes_index, 0);
	}

	private ArrayList<Integer> loadProbabilityBox(int alchemyLevel) throws UnsupportedEncodingException {
		String path = String.format("config/ASmeltingBox%d.txt", alchemyLevel);
		byte[] buff = readFile(path);
		if (buff == null) {
			System.out.println(String.format("%s Üôðíî¤ûäî¤ÔÁö¢ãÁÛ¡ßæÖõÙýð¹¡£", path));
			return null;
		}

		String boxesMessage = new String(buff, "UTF-8");
		String[] boxesInfo = boxesMessage.split("\r\n");
		int count = boxesInfo.length;
		ArrayList<Integer> list = new ArrayList<Integer>(count);
		for (int i = 0; i < count; ++i) {
			String info = boxesInfo[i]
					.trim()
					.replace(",", "");
			if (isNullOrEmpty(info) || info.startsWith("#"))
				continue;
			try {
				list.add(Integer.parseInt(info));
			} catch (Exception e) {
				System.out.println(String.format("î¤%sñéÛ¡úÞÙíÛöÔÁö¢îÜâ¦Ëà£¬íâÐìÔ¯Î¦¡£ => %s", path, info));
				e.printStackTrace();
			}
		}
		if (list == null || list.size() <= 0)
			return null;

		return list;
	}

	private static boolean isNullOrEmpty(String s) {
		return s == null || s.equals("");
	}

	private byte[] readFile(String path) {
		FileInputStream fs = null;
		BufferedInputStream is = null;
		byte[] buff = null;
		try {
			fs = new FileInputStream(path);
			is = new BufferedInputStream(fs);
			buff = new byte[(int) fs.getChannel().size()];
			is.read(buff, 0, buff.length);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.close();
					fs = null;
				} catch (Exception e) {
				}
			}

			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (Exception e) {
				}
			}
		}
		return buff;
	}

	public void dispose() {
		if (m_probability_boxes != null) {
			m_probability_boxes = null;
		}
	}

	@Override
	public void execute(MJCommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}

	private MJCommandTree createCommand() {
		return new MJCommandTree(".ïñÖ¡à´ùì", "[ñìãæÊ¥î°][ôøã·ûùßãìÚ][öèÕ¯]", null) {
			@Override
			protected void to_handle_command(MJCommandArgs args) throws Exception {

			}
		}
				.add_command(new MJCommandTree("ñìãæÊ¥î°", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						reload();
						args.notify("ì«ñìãæÊ¥î°ïñÖ¡à´?áãùì¡£");
					}
				})
				.add_command(new MJCommandTree("ôøã·ûùßãìÚ", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						initialize_index();
						args.notify("ì«ôøã·ûùïñÖ¡à´?áãùìßãìÚ¡£");
					}
				})
				.add_command(new MJCommandTree("öèÕ¯", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						shuffleList();
						args.notify("ì«öèÕ¯ïñÖ¡à´?áãùì¡£");
					}
				});
	}

}