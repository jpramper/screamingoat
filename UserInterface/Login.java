package UserInterface;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import Core.Global;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
	
	private static Login instance = null; 
	
	public static Login getInstance() {
		if (instance == null) {
			instance = new Login();
		}
		return instance;
	}
	
	private static final long serialVersionUID = 1L;
	
	public JPanel contentPane;
	public JTextField txtUser;
	public JTextField txtPass;
	public JLabel lblError;
	public JLabel lblSuccess;
	
	public static void init() {
		Login.getInstance().txtUser.setText("");
		Login.getInstance().txtPass.setText("");
		Login.getInstance().lblError.setText("");
		Login.getInstance().lblSuccess.setText("");
		
		Login.getInstance().txtUser.requestFocusInWindow();
	}
	
	public void signIn() {
		lblError.setText("");
		lblSuccess.setText("");
		
		if (txtUser.getText().trim().equals(""))
		{
			lblError.setText("Username requerido.");
			return;
		}
		if (txtPass.getText().trim().equals(""))
		{
			lblError.setText("Password requerido.");
			return;
		}
		
		// mando al servidor, tipo 10, username~password
		Global.sendMessage(
				10, 
				txtUser.getText().toString() + "~" + txtPass.getText().toString(), 
				Global.serverIp.toString(), 
				1, 
				Global.messagingSocket,
				Global.messagingPort);
	}
	
	public void logIn() {
		lblError.setText("");
		lblSuccess.setText("");
		
		if (txtUser.getText().trim().equals(""))
		{
			lblError.setText("Username requerido.");
			return;
		}
		if (txtPass.getText().trim().equals(""))
		{
			lblError.setText("Password requerido.");
			return;
		}
		
		// mando al servidor tipo 3, username~password
		Global.sendMessage(
				3, 
				txtUser.getText().toString() + "~" + txtPass.getText().toString(), 
				Global.serverIp.toString(), 
				1, 
				Global.messagingSocket,
				Global.messagingPort);
	}

	/**
	 * Create the frame.
	 */
	private Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 335, 221);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUser = new JLabel("Username");
		lblUser.setBounds(12, 12, 97, 15);
		contentPane.add(lblUser);
		
		JLabel lblPass = new JLabel("Password");
		lblPass.setBounds(12, 56, 70, 15);
		contentPane.add(lblPass);
		
		txtUser = new JTextField();
		txtUser.setBounds(110, 10, 189, 19);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		txtPass = new JTextField();
		txtPass.setBounds(110, 54, 189, 19);
		contentPane.add(txtPass);
		txtPass.setColumns(10);
		
		JButton btnSignin = new JButton("Sign In");
		btnSignin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				signIn();
			}
		});
		btnSignin.setBounds(36, 146, 117, 25);
		contentPane.add(btnSignin);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logIn();
			}
		});
		btnLogin.setBounds(182, 146, 117, 25);
		contentPane.add(btnLogin);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setBounds(12, 119, 304, 15);
		contentPane.add(lblError);
		
		lblSuccess = new JLabel("");
		lblSuccess.setForeground(Color.GREEN);
		lblSuccess.setBounds(12, 92, 304, 15);
		contentPane.add(lblSuccess);
	}
}
