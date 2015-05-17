package Main;

import Core.Global;
import UserInterface.Login;

/**
 * Application start point. 
 *  
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class Goat {

	public static void main(String[] args) {
		// initiate server lookup
		Global.searchServer();
		
		// create and present the login window
		Login.init();
		Login.getInstance().setVisible(true);
	}

}
