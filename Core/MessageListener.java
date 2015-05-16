package Core;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;

import Actions.Client;
import Actions.Server;
import Actions.User;
import StringUtils.Parsers;
import UserInterface.ChatWindow;
import UserInterface.Login;


public class MessageListener implements Runnable {
	public boolean isListening;
	
	public void run(){
		isListening = true;
		DatagramPacket receivePacket;
		byte[] receiveData;
		
		if (Global.DEBUG) 
    		System.out.println("MessageListener.run()| " + 
    	"listener started.");

	    while(isListening){
	    	try {
	    		receiveData = new byte[1024];
	    		receivePacket = new DatagramPacket(receiveData, receiveData.length);
	    		
	    		Global.messagingSocket.receive(receivePacket);
	    		
	    		DataPacket pkg = DataPacket.parseDataPacket(receivePacket);
	    		String address = receivePacket.getAddress().toString();
	    		
	    		processMsg(pkg, address);
	    	}
	    	catch (SocketTimeoutException e) {
	    		continue;
	    	}
	    	catch (Exception e) {
				e.printStackTrace();
				isListening = false;
	    	}
	    }
	}
	
	public void processMsg(DataPacket pkg, String address) {
		//parse pkg
		int destType = pkg.destType;
		pkg.data = pkg.data.trim();
		
		switch (pkg.dataType) {
			
			case 1: //msg
				if(destType == 1){ //receive msg and send it to the correct one
					String sender = "";
					for(User usr : Server.active){
						if(usr.ip.equals(address)){
							sender = usr.nickname;
						}
					}
					
					String[] datas = new String[2];
					datas = Server.privateMessage(pkg.data);
					
					String person = Server.returnPerson(datas[0], sender);
					if( person == null){
						//no existe
					}else if (person.equals("banned")){
						// usuario bloqueado.
					}else{
						Global.sendMessage(1, sender+": " + datas[1], person, 0, Global.messagingSocket);
					}
					
				} else if(destType == 0){ // you got mail.!!
					//print it pkg.data
				}
				break;
				
			case 2:	//boadcast	
				if(destType == 1){ //receive msg and broadcast it
					
					String sender = "";
					for(User usr : Server.active){
						if(usr.ip.equals(address)){
							sender = usr.nickname;
						}
					}
					Global.sendBroadcast(2, sender + ": " + pkg.data , 0,sender, Global.messagingSocket);
					
					
				} else if(destType == 0){ // you got a public mail.
					//print pkg.data
				}
				break;
				
				
			case 3: //login	
				
				if(destType == 1){ //receive loggin request
					String[] dtalgin = Parsers.parseSignLogin(pkg.data);
					int ret = Server.logginUser(dtalgin[0], dtalgin[1]);
					
					switch (ret){
						case 0:
							Global.sendMessage(3, "0", address,0, Global.messagingSocket); //success loggin in
							syncClocks();
							break;
						case 1:
							Global.sendMessage(3, "1", address,0, Global.messagingSocket); //incorrect name
							break;
						case 2:
							Global.sendMessage(3, "2", address,0, Global.messagingSocket); //incorrect password
							break;
						default:
							break;
					}
					
					
				} else if(destType == 0){ // server response 0 success, 1 name, 2 password
					Login loginWindow = Login.getInstance();
					switch (Integer.parseInt(pkg.data)){
					
					case 0:
						loginWindow.lblSuccess.setText("loggeandote");
						loginWindow.lblSuccess.setText("sincronizando reloj con berkley");
						syncClocks();
						
						Login.getInstance().setVisible(false);
						
						ChatWindow.getInstance().setVisible(true);
						ChatWindow.getInstance().txtNickname.setText(
								Login.getInstance().txtUser.getText().trim());
						//success loggin in
						//send syincClocks routine
						break;
					case 1:
						loginWindow.lblError.setText("compilla... la cagaste con el nombre");
						//incorrect name
						break;
					case 2:
						loginWindow.lblError.setText("mmm... ese no es tu passbordio");
						//incorrect password
						break;
					case 3:
						loginWindow.lblError.setText("user already logged in (osea... no puedes 2 veces)");
						//incorrect password
						break;
						
					default:
						break;
					}
				}
				
				break;
				
				
				
			//shareReq	
			case 4:
				if(destType == 1){ //receive request of share active users
					Global.sendMessage(4, Server.actives(), address,0, Global.messagingSocket);
				} else if(destType == 0){ // receive active users
					ChatWindow.getInstance().txtIncoming.setText(
							"Están conectados: " + pkg.data.trim() + "\n" +
							ChatWindow.getInstance().txtIncoming.getText()
							);
					// pkg.data contains a concat string of the active users
				}
				break;
				
				
			//bann list?	data = nickname of the banned
			case 5:
				if(destType == 1){ //receive request to bann a person
					Server.banPerson(pkg.data ,address);
				} else if(destType == 0){ // ... do nothing
					
				}
				break;
				
			//ack	
			case 6:
				break;
				
			//exit	
			case 7:
				Server.removeActiveUser(address);
				Global.sendMessage(6, "", address,0, Global.messagingSocket);
				break;
				
			//error	
			case 8:
				break;
				
			//handshake
			case 9:
				Global.sendMessage(9, "", address,0, Global.messagingSocket);
				break;
				
			//sign in
			case 10:
				String[] dtasgnin = Parsers.parseSignLogin(pkg.data);
				
				if(destType == 1){ //sign in request
					if(Server.createUser(dtasgnin[0], address ,dtasgnin[1]))
						Global.sendMessage(10, "0", address,0, Global.messagingSocket); //success sign in
					else
						Global.sendMessage(10, "1", address,0, Global.messagingSocket); //name already taken
					
					
				} else if(destType == 0){ // answer from server
					Login loginWindow = Login.getInstance();
					System.out.println(pkg.data.length());
					switch (Integer.parseInt(pkg.data)){
					
					case 0:
						//success loggin in
						loginWindow.lblSuccess.setText("si te metiste chido");
						break;
					case 1:
						//name taken
						loginWindow.lblError.setText("compilla tu nombre esta bien usado");
						break;
						
					default:
						System.out.println("no junca");
						break;
					}
				}
				break;
				
				
			//time	comm
			case 11:
				if(destType == 1){ //server mode (receive time and process it)
					Server.saveDate(pkg.data,address);
					if(Server.checkifLastTIme()){
						
						Server.berkley();
						System.out.println("sinz and sending delta");
						for (User usr : Server.active) {
							Global.sendMessage(12, usr.deltatime+"", usr.ip, 0, Global.messagingSocket);
						}
						Server.endActClock();
					}
				}else if (destType == 0){ // Client mode (send my timestamp)
					String time = Client.getTime();
					Global.sendMessage(11,time,Global.serverIp.toString(),1, Global.messagingSocket);
				}
				
			//time setup	
			case 12:
				if(destType == 1){ //server ... do nothing
					
				}else if (destType == 0){ //set my delta
					for (User usr : Server.active) {
						if(usr.ip.equals(address))
							usr.deltatime = Float.parseFloat(pkg.data);
					}
				}
			default:
				break;
		
		}
	}
	
	public String syncClocks(){
		Global.sendBroadcast(11,"",0,"", Global.messagingSocket);
		return "";
	}
	
}
