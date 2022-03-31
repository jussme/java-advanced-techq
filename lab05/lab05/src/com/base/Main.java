package com.base;

import java.awt.EventQueue;

import com.gui.AppWindow;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new AppWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
