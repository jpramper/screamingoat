import UserInterface.Login;



public class Goat {

	public static void main(String[] args) {
		// initiate the connection manager
		ConnectionManager cm = ConnectionManager.getInstance();
		cm.buscaServidor();
		
		// create and present the login window
		Login login = Login.getInstance();
		login.setVisible(true);
	}

}
