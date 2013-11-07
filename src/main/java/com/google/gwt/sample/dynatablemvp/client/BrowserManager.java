package com.google.gwt.sample.dynatablemvp.client;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingHandler;

public class BrowserManager implements FavoritesManager.CookieStorage {

	@Override
	public String getCookie(String name) {
		return Cookies.getCookie(name);
	}

	@Override
	public void setCookie(String name, String value) {
		Cookies.setCookie(name,value);
	}

	@Override
	public void addWindowClosingHandler(ClosingHandler handler) {
		Window.addWindowClosingHandler(handler);
	}

}
