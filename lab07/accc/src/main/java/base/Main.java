package base;

import gui.AppWindow;
import hibernate.HibernateUtil;

public class Main {
	public static void main(String[] args) {
		HibernateUtil.populateDB();
		new AppWindow(new DemoMediator());
	}
}
