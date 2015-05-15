package UserInterface;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

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
	
	private JPanel contentPane;
	private JTextField txtUser;
	private JTextField txtPass;
	
	public void signIn() {
		
	}
	
	public void logIn() {
		
	}

	/**
	 * Create the frame.
	 */
	private Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUser = new JLabel("User");
		lblUser.setBounds(39, 82, 70, 15);
		contentPane.add(lblUser);
		
		JLabel lblPass = new JLabel("Pass");
		lblPass.setBounds(39, 135, 70, 15);
		contentPane.add(lblPass);
		
		txtUser = new JTextField();
		txtUser.setBounds(132, 80, 114, 19);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		txtPass = new JTextField();
		txtPass.setBounds(132, 133, 114, 19);
		contentPane.add(txtPass);
		txtPass.setColumns(10);
		
		JButton btnSignin = new JButton("Sign In");
		btnSignin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				signIn();
			}
		});
		btnSignin.setBounds(39, 221, 117, 25);
		contentPane.add(btnSignin);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logIn();
			}
		});
		btnLogin.setBounds(199, 221, 117, 25);
		contentPane.add(btnLogin);
		
		JLabel lblError = new JLabel("Error");
		lblError.setForeground(Color.RED);
		lblError.setBounds(56, 183, 70, 15);
		contentPane.add(lblError);
	}
}
