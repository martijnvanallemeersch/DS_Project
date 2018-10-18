package ds_project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class login extends JFrame {

	private JPanel contentPane;
	private JTextField tfUserName;
	private JTextField tfPassWord;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 508, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUserName = new JLabel("Username");
		lblUserName.setBounds(5, 5, 233, 85);
		contentPane.add(lblUserName);
		
		tfUserName = new JTextField();
		tfUserName.setBounds(248, 5, 233, 85);
		contentPane.add(tfUserName);
		tfUserName.setColumns(10);
		
		JLabel lblPassWord = new JLabel("Password");
		lblPassWord.setBounds(5, 100, 233, 85);
		contentPane.add(lblPassWord);
		
		tfPassWord = new JTextField();
		tfPassWord.setBounds(248, 100, 233, 85);
		contentPane.add(tfPassWord);
		tfPassWord.setColumns(10);
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.setBounds(238, 216, 250, 85);
		contentPane.add(btnLogIn);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lobby lobby = new lobby();
				lobby.setVisible(true);				
			}
		});
		btnRegister.setBounds(0, 216, 250, 85);
		contentPane.add(btnRegister);
	}
}
