package ds_project;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class lobby extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					lobby frame = new lobby();
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
	public lobby() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblExistingGame = new JLabel("Choose an existing game");
		lblExistingGame.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblExistingGame.setBounds(21, 34, 167, 30);
		contentPane.add(lblExistingGame);
		
		JComboBox cbExistingGame = new JComboBox();
		cbExistingGame.setBounds(256, 41, 101, 21);
		contentPane.add(cbExistingGame);
		
		JLabel lbCreateNewGame = new JLabel("Create new game");
		lbCreateNewGame.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbCreateNewGame.setBounds(21, 85, 167, 30);
		contentPane.add(lbCreateNewGame);
		
		JComboBox cbCreateNewGame = new JComboBox();
		cbCreateNewGame.setBounds(256, 95, 101, 21);
		cbCreateNewGame.addItem("3X3");
		cbCreateNewGame.addItem("4X4");
		contentPane.add(cbCreateNewGame);
		
		JButton btnLobby = new JButton("Play");
		btnLobby.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(cbCreateNewGame.getSelectedItem() == "3X3")
				{
					memory_mainView ticTacToe = new memory_mainView(3,160);
				}
				else if(cbCreateNewGame.getSelectedItem() == "4X4")
				{
					memory_mainView ticTacToe = new memory_mainView(4,125);
				}
				
			}
		});
		btnLobby.setBounds(150, 153, 116, 30);
		contentPane.add(btnLobby);
	}
}
