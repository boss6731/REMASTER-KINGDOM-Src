package l1j.server.MJWebServer.Dispatcher.my.service;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJString;

import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyAppDwonloadMapped;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyAppDwonloadMapped.MJMyAppDwonloadInfo;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyShortcutMapped;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyShortcutMapped.MJMyShortcutInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService.AppDownloadMenuMapped.AppDownloadItems;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService.MenuMapped.MenuMappedCategory;
import l1j.server.MJWebServer.Dispatcher.my.service.MJMyApiPageService.ShortcutMenuMapped.ShortcutMenuMappedCategory;

public class MJMyApiPageService {
	private static final MJMyApiPageService service = new MJMyApiPageService();
	public static MJMyApiPageService service(){
		return service;
	}

	private MenuMapped menuMapped;
	private MenuMapped gmMenuMapped;
	private ShortcutMenuMapped shortcutMenuMapped;
	private AppDownloadMenuMapped appDownloadMenuMapped;
	private MJMyApiPageService(){
		menuMapped = new MenuMapped();
		gmMenuMapped = new MenuMapped();
		shortcutMenuMapped = new ShortcutMenuMapped();
		appDownloadMenuMapped = new AppDownloadMenuMapped();
	}
	
	public MenuMapped menuMapped(){
		return menuMapped;
	}
	
	public MenuMapped gmMenuMapped(){
		return gmMenuMapped;
	}
	
	public ShortcutMenuMapped shortcutMenuMapped(){
		return shortcutMenuMapped;
	}
	
	public AppDownloadMenuMapped appDownloadMenuMapped(){
		return appDownloadMenuMapped;
	}
	
	public void onMenuChanged(MJMyPageMapped pageMapped){
		MenuMapped menuMapped = new MenuMapped();
		MenuMapped gmMenuMapped = new MenuMapped();
		menuMapped.accountsURL = pageMapped.accountsURL();
		gmMenuMapped.accountsURL = pageMapped.accountsURL();
		if(pageMapped.categories() != null && pageMapped.categories().size() > 0){
			menuMapped.categories = new ArrayList<>(pageMapped.categories().size());
			gmMenuMapped.categories = new ArrayList<>(pageMapped.categories().size());
			for(MJMyPageInfo pInfo : pageMapped.categories()){
				if(!pInfo.menu()){
					continue;
				}
				
				MenuMappedCategory category = new MenuMappedCategory();
				category.name = pInfo.name();
				category.uri = pInfo.uri();
				category.category = pInfo.category();
				category.desc = pInfo.desc();
				category.notice = pInfo.notice();
				
				gmMenuMapped.categories.add(category);	
				if(!pInfo.gm()){
					menuMapped.categories.add(category);	
				}
			}
		}
		this.menuMapped = menuMapped;
		this.gmMenuMapped = gmMenuMapped;
	}
	
	public void onShortcutMenuMapped(MJMyShortcutMapped shortcutMapped){
		ShortcutMenuMapped shortcutMenuMapped = new ShortcutMenuMapped();
		if(shortcutMapped.categories() != null && shortcutMapped.categories().size() > 0){
			for(MJMyShortcutInfo sInfo : shortcutMapped.categories()){
				ShortcutMenuMappedCategory category = new ShortcutMenuMappedCategory();
				category.id = sInfo.id();
				category.name = sInfo.name();
				category.newWindow = sInfo.newWindow();
				category.shortcutClassName = sInfo.shortcutClassName();
				category.uri = sInfo.uri();
				shortcutMenuMapped.categories.add(category);
			}
		}
		this.shortcutMenuMapped = shortcutMenuMapped;
	}
	
	public void onAppDownloadChanged(MJMyAppDwonloadMapped appsMapped){
		AppDownloadMenuMapped appDownloadMenuMapped = new AppDownloadMenuMapped();
		if(appsMapped.apps() != null && appsMapped.apps().size() > 0){
			for(MJMyAppDwonloadInfo aInfo : appsMapped.apps()){
				AppDownloadItems loadItem = new AppDownloadItems();
				loadItem.img = aInfo.img();
				loadItem.name = aInfo.name();
				loadItem.uri = aInfo.uri();
				appDownloadMenuMapped.apps.add(loadItem);
			}
		}
		this.appDownloadMenuMapped = appDownloadMenuMapped;
	}
	
	public static class MenuMapped{
		private String accountsURL;
		private ArrayList<MenuMappedCategory> categories;
		private MenuMapped(){
			accountsURL = MJString.EmptyString;
			categories = new ArrayList<>();
		}
		
		public String accountsURL(){
			return accountsURL;
		}
		
		public ArrayList<MenuMappedCategory> categories(){
			return categories;
		}
		
		public static class MenuMappedCategory{
			private String name;
			private int category;
			private String uri;
			private String desc;
			private boolean notice;
			private MenuMappedCategory(){
				name = MJString.EmptyString;
				category = -1;
				uri = MJString.EmptyString;
				desc = MJString.EmptyString;
				notice = false;
			}
			
			public String name(){
				return name;
			}
			
			public int category(){
				return category;
			}
			
			public String uri(){
				return uri;
			}
			
			public String desc(){
				return desc;
			}
			
			public boolean notice(){
				return notice;
			}
		}
	}
	
	public static class ShortcutMenuMapped{
		private ArrayList<ShortcutMenuMappedCategory> categories;
		private ShortcutMenuMapped(){
			categories = new ArrayList<>();
		}
		
		public ArrayList<ShortcutMenuMappedCategory> categories(){
			return categories;
		}
		
		public static class ShortcutMenuMappedCategory{
			private String name;
			private boolean newWindow;
			private String uri;
			private String shortcutClassName;
			private String id;
			private ShortcutMenuMappedCategory(){
				name = MJString.EmptyString;
				newWindow = false;
				uri = MJString.EmptyString;
				shortcutClassName = MJString.EmptyString;
				id = MJString.EmptyString;
			}
			
			public String name(){
				return name;
			}
			
			public boolean newWindow(){
				return newWindow;
			}
			
			public String uri(){
				return uri;
			}
			
			public String shortcutClassName(){
				return shortcutClassName;
			}
			
			public String id(){
				return id;
			}
		}
	}
	
	public static class AppDownloadMenuMapped{
		private ArrayList<AppDownloadItems> apps;
		private AppDownloadMenuMapped(){
			apps = new ArrayList<>();
		}
		
		public ArrayList<AppDownloadItems> apps(){
			return apps;
		}
		
		public static class AppDownloadItems{
			private String name;
			private String uri;
			private String img;
			private AppDownloadItems(){
				name = MJString.EmptyString;
				uri = MJString.EmptyString;
				img = MJString.EmptyString;
			}
			
			public String name(){
				return name;
			}
			
			public String uri(){
				return uri;
			}
			
			public String img(){
				return img;
			}
		}
	}
}
