package spellChecker;

import java.awt.EventQueue;

import spellChecker.view.Ui;

public class SpellCheckerApp {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					Ui window = new Ui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
