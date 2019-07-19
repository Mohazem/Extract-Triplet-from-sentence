package main;

import gui.MainFrame;
import parsers.StanfordNLParser;
import utils.ETInput;
import utils.ParserDriver;

public class Main {
	public static void main(String[] args) {
		//just init the mainframe
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ex) {
			
		}

		/* Create and display the form */
				new MainFrame().setVisible(true);

	}
}
