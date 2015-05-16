package UserInterface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Core.Global;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;

/**
 * Presents an interface to the user to interact with the chat program.
 * 
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class ChatWindow extends JFrame {
	
	private static ChatWindow instance = null; 
	
	public static ChatWindow getInstance() {
		if (instance == null) {
			instance = new ChatWindow();
		}
		return instance;
	}
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel lblSuNickname;
	private JLabel lblEscribaAqui;
	public JTextArea txtIncoming;
	public JTextField txtOutgoing;
	private JButton btnEnviar;
	public JTextField txtNickname;
	private JMenuItem mntmClima;
	private JMenuItem mntmJuego;
	private JMenuItem mntmQuienEsta;
	private JMenuItem mntmCerrarSesin;
	public JCheckBox chkIsServer;
	
	public static void init() {
		ChatWindow.getInstance().txtOutgoing.requestFocusInWindow();
		ChatWindow.getInstance().chkIsServer.setSelected(false);
		ChatWindow.getInstance().txtIncoming.setText("");
		ChatWindow.getInstance().txtOutgoing.setText("");
	}
	
	public void sendMessage() {
		// mando al servidor, tipo 10, username~password
		String message = txtOutgoing.getText().trim();
		int messageType = 2; // assume a broadcast by default
		
		if (message.equals("")) return;
		
		if (message.startsWith("@")){
			// private message
			if (!message.contains(" ")) return;
			
			// extract the user
			String user = message.split(" ")[0].replace("@", "");
			message = message.substring(user.length()+2);
			
			message = user + "~" + message;
			
			messageType = 1; // set type to private
		}else if (message.startsWith("#")){
			//mute User
			message = message.replace("#", "");
			messageType = 5;
		}else if (message.startsWith("%")){
			//unmute User
			message = message.replace("%", "");
			messageType = 6;
		}
		
		Global.sendMessage(
				messageType, 
				message, 
				Global.serverIp.toString(), 
				1, 
				Global.messagingSocket,
				Global.messagingPort);
		
		txtOutgoing.setText("");
	}
	
	public void whoIsOnline() {
		// mando al servidor, tipo 10, username~password
		Global.sendMessage(
				4, 
				"", 
				Global.serverIp.toString(), 
				1, 
				Global.messagingSocket,
				Global.messagingPort);
	}
	
	public void climateCheck() {
		
	}
	
	public void launchGame() {
		
	}
	
	public void sendFile() {
		
	}
	
	public void muteUser(){
		Global.sendMessage(
				5, 
				"", 
				Global.serverIp.toString(), 
				1, 
				Global.messagingSocket,
				Global.messagingPort);
	}
	
	public void logout() {
		// mando al servidor, tipo 10, username~password
		Global.sendMessage(
				7, 
				"", 
				Global.serverIp.toString(), 
				1, 
				Global.messagingSocket,
				Global.messagingPort);
		
		ChatWindow.init();
		ChatWindow.getInstance().setVisible(false);
		
		Login.init();
		Login.getInstance().setVisible(true);
	}
	
	private ChatWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 537, 338);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mntmQuienEsta = new JMenuItem("Quién Está?");
		mntmQuienEsta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				whoIsOnline();
			}
		});
		menuBar.add(mntmQuienEsta);
		
		mntmClima = new JMenuItem("Clima");
		mntmClima.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				climateCheck();
			}
		});
		menuBar.add(mntmClima);
		
		JMenuItem mntmArchivo = new JMenuItem("Archivo");
		mntmArchivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendFile();
			}
		});
		menuBar.add(mntmArchivo);
		
		mntmJuego = new JMenuItem("Juego!");
		mntmJuego.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				launchGame();
			}
		});
		menuBar.add(mntmJuego);
		
		mntmCerrarSesin = new JMenuItem("Cerrar Sesión");
		mntmCerrarSesin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				logout();
			}
		});
		menuBar.add(mntmCerrarSesin);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtIncoming = new JTextArea();
		txtIncoming.setEditable(false);
		txtIncoming.setBounds(12, 43, 513, 122);
		contentPane.add(txtIncoming);
		
		txtOutgoing = new JTextField();
		txtOutgoing.setBounds(12, 204, 513, 19);
		contentPane.add(txtOutgoing);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendMessage();
			}
		});
		btnEnviar.setBounds(408, 235, 117, 25);
		contentPane.add(btnEnviar);
		
		txtNickname = new JTextField();
		txtNickname.setText("Anonimo");
		txtNickname.setBounds(137, 12, 114, 19);
		contentPane.add(txtNickname);
		txtNickname.setColumns(10);
		txtNickname.setEditable(false);
		
		lblSuNickname = new JLabel("Su Nickname:");
		lblSuNickname.setBounds(12, 14, 95, 15);
		contentPane.add(lblSuNickname);
		
		lblEscribaAqui = new JLabel("Escriba Aqui:");
		lblEscribaAqui.setBounds(12, 177, 114, 15);
		contentPane.add(lblEscribaAqui);
		
		getRootPane().setDefaultButton(btnEnviar);
		
		chkIsServer = new JCheckBox("");
		chkIsServer.setBounds(502, 12, 23, 23);
		chkIsServer.setEnabled(false);
		contentPane.add(chkIsServer);
	}
}
