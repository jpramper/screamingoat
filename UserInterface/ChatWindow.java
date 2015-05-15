package UserInterface;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JLabel;

/**
 * Presents an interface to the user to interact with the chat program.
 * 
 * @author Juan Pablo Ramírez
 * @author Alejandro Rojas
 *
 */
public class ChatWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JLabel lblSuNickname;
	private JLabel lblEscribaAqui;
	private JTextArea txtIncoming;
	private JTextField txtOutgoing;
	private JButton btnEnviar;
	private JTextField txtNickname;
	private JButton btnConectar;
	private JButton btnConectados;
	
	public ChatWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtIncoming = new JTextArea();
		txtIncoming.setEditable(false);
		txtIncoming.setBounds(12, 43, 426, 122);
		contentPane.add(txtIncoming);
		
		txtOutgoing = new JTextField();
		txtOutgoing.setBounds(12, 204, 426, 19);
		contentPane.add(txtOutgoing);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setEnabled(false);
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
			}
		});
		btnEnviar.setBounds(144, 235, 117, 25);
		contentPane.add(btnEnviar);
		
		txtNickname = new JTextField();
		txtNickname.setText("Anonimo");
		txtNickname.setBounds(137, 12, 114, 19);
		contentPane.add(txtNickname);
		txtNickname.setColumns(10);
		
		lblSuNickname = new JLabel("Su Nickname:");
		lblSuNickname.setBounds(12, 14, 95, 15);
		contentPane.add(lblSuNickname);
		
		lblEscribaAqui = new JLabel("Escriba Aqui:");
		lblEscribaAqui.setBounds(12, 177, 114, 15);
		contentPane.add(lblEscribaAqui);
		
		btnConectar = new JButton("Conectar!");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
			}
		});
		btnConectar.setBounds(297, 9, 117, 25);
		contentPane.add(btnConectar);
		
		btnConectados = new JButton("Quien Está?");
		btnConectados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
			}
		});
		btnConectados.setEnabled(false);
		btnConectados.setBounds(321, 235, 117, 25);
		contentPane.add(btnConectados);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow frame = new ChatWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
