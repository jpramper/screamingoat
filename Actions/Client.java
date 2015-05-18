package Actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Core.Global;
import Files.FileEvent;
import Files.FileGoat;
import StringUtils.Parsers;

public class Client {
	public static String nickname = "";
	
	public static String getTime(){
		
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String Sdate = dateFormat.format(date);
		
		return Sdate;
	}
	
	public static void reTimeStamp(String name){
		Date date = new Date();
		String[] dta = new String[2];
		float secs = 0;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String Sdate = dateFormat.format(date);
		secs = 0;
		dta = Parsers.parseTime(Sdate);
		secs = Integer.parseInt(dta[0]) * 60;
		secs = secs + Integer.parseInt(dta[1]);
		secs *= 60;
		secs += Integer.parseInt(dta[2]);
		
		
		for(User usr: Server.users){
			if(usr.nickname.equals(name))
			usr.secstimestamp = secs;
		}
		
		
	}
	
	public static void saveAndSincData(String allData){
		ArrayList<User> Newusers = new ArrayList<User>();
		System.out.println("ALL DATA");
		System.out.println(allData);
		
		String[] dta = new String[2];
		dta = allData.split("%");
		//dta[0] -> numero de usuarios
		//dta[1] -> usuarios
		
		String[] usuarios = new String[Integer.parseInt(dta[0])];
		usuarios = dta[1].split("~");
		for(int i = 0; i < Integer.parseInt(dta[0]); i++){
			String[] info = new String[8];
			info = usuarios[i].split(",");
			System.out.println("estoy hay en 6 " + info[6]);
			User u = new User(info[0], info[1], info[2],info[3],info[4],info[5],info[6],info[7]);
			Newusers.add(u);
		}
		Server.users = Newusers;
		
	}
	
	public static String displayClientTime(){
		String time = "";
		float secs = 0;
		for(User usr: Server.users){
			if(usr.nickname.equals(nickname)){
				reTimeStamp(nickname);
				secs = usr.secstimestamp + usr.deltatime;
			}
		}
		System.out.println("estos son mis segundos: " + secs);
		time = Server.secsToTime(secs);
		return time;
	}
	
	public static String sendFile(String ndestiny){
		FileEvent event = null;
		String ipdest = Server.returnIp("/home/cury/Desktop/Heuristica");
		
		try {
			event = FileGoat.getFileEvent(ndestiny);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(event);
			byte[] data = outputStream.toByteArray();
			
			Global.sendMessage(18, data.toString(), ipdest, 0, Global.messagingSocket, Global.messagingPort);
			
		}catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return "";
	}
	
	public static void sendFile(){
		
	}

}
