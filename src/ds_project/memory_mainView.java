package ds_project;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class memory_mainView implements Runnable {

	private String ip = "localhost";
	private int port = 22222;
	private Scanner scanner = new Scanner(System.in);
	private JFrame frame;
	private final int WIDTH = 520; //506
	private final int HEIGHT = 540;
	private Thread thread;

	private Painter painter;
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;

	private ServerSocket serverSocket;

	private BufferedImage board;
	private BufferedImage redX;
	private BufferedImage blueX;
	private BufferedImage redCircle;
	private BufferedImage blueCircle;

	private Boolean[] spaces;
	private BufferedImage[] spaces2;

	private BufferedImage[] ImageArray;

	private boolean yourTurn = false;
	private boolean circle = true;
	private boolean accepted = false;
	private boolean unableToCommunicateWithOpponent = false;
	private boolean won = false;
	private boolean enemyWon = false;
	private boolean tie = false;

	private int lengthOfSpace;	
	private int groteGame;
	private boolean [] checkMemory;

	private int errors = 0;
	private int firstSpot = -1;
	private int secondSpot = -1;

	private Font font = new Font("Verdana", Font.BOLD, 32);
	private Font smallerFont = new Font("Verdana", Font.BOLD, 20);
	private Font largerFont = new Font("Verdana", Font.BOLD, 50);

	private String waitingString = "Waiting for another player";
	private String unableToCommunicateWithOpponentString = "Unable to communicate with opponent.";

	
	Map<Integer, Integer> GelinkteKaartenMemory;
	int[] checkforGelinkteKaarten;
	int indexClick = 0;
	boolean tweeZetten = false;
	int indexRead = 0;
	int[] readValues;

	
	public memory_mainView(int groteGameArg,int lengthOfSpaceArg) {
		
			
		this.lengthOfSpace = lengthOfSpaceArg;
		this.groteGame = groteGameArg;
		spaces = new Boolean[groteGameArg * groteGameArg];
		
		for (int i = 0; i < spaces.length; i++) {
			spaces[i] = false;
		}
		
		spaces2 = new BufferedImage[groteGameArg * groteGameArg];
		ImageArray = new BufferedImage[groteGameArg * groteGameArg];
		checkMemory = new boolean [2];
		GelinkteKaartenMemory = new HashMap<Integer, Integer>();
		checkforGelinkteKaarten = new int[2];
		checkforGelinkteKaarten[0]= -1;
		checkforGelinkteKaarten[1]= -1;

		readValues = new int[2];
		//fillSpaces();	
		//System.out.println("Please input the IP: ");
		//ip = scanner.nextLine();
		ip = "localhost";
		//System.out.println("Please input the port: ");
		//port = scanner.nextInt();
		port = 22222;
		while (port < 1 || port > 65535) {
			System.out.println("The port you entered was invalid, please input another port: ");
			port = scanner.nextInt();
		}

		loadImages();

		painter = new Painter();
		painter.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		if (!connect()) initializeServer();

		frame = new JFrame();
		frame.setTitle("memory");
		frame.setContentPane(painter);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);

		thread = new Thread(this, "TicTacToe");
		thread.start();
	}

	public void run() {
		while (true) {
			tick();
			painter.repaint();

			if (!circle && !accepted) {
				listenForServerRequest();
			}
		}
	}

	private void render(Graphics g) {
		g.drawImage(board, 0, 0, null);
		if (unableToCommunicateWithOpponent) {
			g.setColor(Color.RED);
			g.setFont(smallerFont);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			int stringWidth = g2.getFontMetrics().stringWidth(unableToCommunicateWithOpponentString);
			g.drawString(unableToCommunicateWithOpponentString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
			return;
		}

		if (accepted) {
			
			if(checkforGelinkteKaarten[0] != -1)
			{				
				//System.out.println("kaart "+ checkforGelinkteKaarten[0] + " index " + indexClick);
				
				if(indexClick == 0)
				{					
					g.drawImage(ImageArray[checkforGelinkteKaarten[0]], (checkforGelinkteKaarten[0] % groteGame) * lengthOfSpace + 10 * (checkforGelinkteKaarten[0] % groteGame), (int) (checkforGelinkteKaarten[0] / groteGame) * lengthOfSpace + 10 * (int) (checkforGelinkteKaarten[0] / groteGame), null);				
				}
				else
				{	
					if(checkforGelinkteKaarten[1] != -1)
					{
						System.out.println("kaart "+ checkforGelinkteKaarten[1] + " index " + indexClick);

						g.drawImage(ImageArray[checkforGelinkteKaarten[1]], (checkforGelinkteKaarten[1] % groteGame) * lengthOfSpace + 10 * (checkforGelinkteKaarten[1] % groteGame), (int) (checkforGelinkteKaarten[1] / groteGame) * lengthOfSpace + 10 * (int) (checkforGelinkteKaarten[1] / groteGame), null);				

						checkMatch();

					}
					g.drawImage(ImageArray[checkforGelinkteKaarten[0]], (checkforGelinkteKaarten[0] % groteGame) * lengthOfSpace + 10 * (checkforGelinkteKaarten[0] % groteGame), (int) (checkforGelinkteKaarten[0] / groteGame) * lengthOfSpace + 10 * (int) (checkforGelinkteKaarten[0] / groteGame), null);				

				}
			}
			
			for (int i = 0; i < spaces.length; i++) {
				if (spaces[i] == true) {
					g.drawImage(ImageArray[i], (i % groteGame) * lengthOfSpace + 10 * (i % groteGame), (int) (i / groteGame) * lengthOfSpace + 10 * (int) (i / groteGame), null);				
				}
			}
			
		} else {
			g.setColor(Color.RED);
			g.setFont(font);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			int stringWidth = g2.getFontMetrics().stringWidth(waitingString);
			g.drawString(waitingString, WIDTH / 2 - stringWidth / 2, HEIGHT / 2);
		}
	}
	
	private void checkMatch()
	{	

		if((GelinkteKaartenMemory.get(checkforGelinkteKaarten[0]) == checkforGelinkteKaarten[1]))
		{
			//System.out.println("GelinkteKaartenMemory");

			spaces[checkforGelinkteKaarten[0]] = true;
			spaces[checkforGelinkteKaarten[1]] = true;
		}		
	}
	private void tick() {
		if (errors >= 10) unableToCommunicateWithOpponent = true;

		if (!yourTurn && !unableToCommunicateWithOpponent) {
			try {
				readValues[indexRead] = dis.readInt();
				System.out.println("read " + readValues[indexRead]);
							


				if(indexRead == 0)
				{
					checkforGelinkteKaarten[0] = readValues[0];
					indexRead = 1 ;
				}
				else //indexRead == 1
				{
					checkforGelinkteKaarten[0] = readValues[0];
					checkforGelinkteKaarten[1] = readValues[1];
					System.out.println("ontvangen"+ checkforGelinkteKaarten[0]);
					System.out.println("ontvangen" + checkforGelinkteKaarten[1]);

					checkMatch();					

					checkForEnemyWin();
					checkForTie();
					indexRead = 0;
					yourTurn = true;
				}
				
			
				
			} catch (IOException e) {
				e.printStackTrace();
				errors++;
			}
		}
	}

	private void checkForWin() {
		
	}

	private void checkForEnemyWin() {
		
	}

	private void checkForTie() {
		
	}

	private void listenForServerRequest() {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			accepted = true;
			System.out.println("CLIENT HAS REQUESTED TO JOIN, AND WE HAVE ACCEPTED");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean connect() {
		try {
			socket = new Socket(ip, port);
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			accepted = true;
		} catch (IOException e) {
			System.out.println("Unable to connect to the address: " + ip + ":" + port + " | Starting a server");
			return false;
		}
		System.out.println("Successfully connected to the server.");
		return true;
	}

	private void initializeServer() {
		try {
			serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
		} catch (Exception e) {
			e.printStackTrace();
		}
		yourTurn = true;
		circle = false;
	}

	private void loadImages() {
		try {		
			
			if(groteGame == 3)
			{
				
			}
			else
			{
				board = ImageIO.read(getClass().getResourceAsStream("/board4X4.png"));
				ImageArray[0]= ImageIO.read(getClass().getResourceAsStream("/audi4.jpg"));
				ImageArray[5]= ImageIO.read(getClass().getResourceAsStream("/audi4.jpg"));
				ImageArray[1]= ImageIO.read(getClass().getResourceAsStream("/bmw4.jpg"));
				ImageArray[6]= ImageIO.read(getClass().getResourceAsStream("/bmw4.jpg"));
				ImageArray[2]= ImageIO.read(getClass().getResourceAsStream("/citroen4.jpg"));
				ImageArray[7]= ImageIO.read(getClass().getResourceAsStream("/citroen4.jpg"));
				ImageArray[3]= ImageIO.read(getClass().getResourceAsStream("/mercedes4.jpg"));
				ImageArray[8]= ImageIO.read(getClass().getResourceAsStream("/mercedes4.jpg"));
				ImageArray[4]= ImageIO.read(getClass().getResourceAsStream("/mini4.jpg"));
				ImageArray[9]= ImageIO.read(getClass().getResourceAsStream("/mini4.jpg"));
				ImageArray[10]= ImageIO.read(getClass().getResourceAsStream("/peugeot4.jpg"));
				ImageArray[14]= ImageIO.read(getClass().getResourceAsStream("/peugeot4.jpg"));
				ImageArray[11]= ImageIO.read(getClass().getResourceAsStream("/porsche4.jpg"));
				ImageArray[15]= ImageIO.read(getClass().getResourceAsStream("/porsche4.jpg"));
				ImageArray[12]= ImageIO.read(getClass().getResourceAsStream("/volkswagen4.jpg"));
				ImageArray[13]= ImageIO.read(getClass().getResourceAsStream("/volkswagen4.jpg"));			
				
				GelinkteKaartenMemory.put(0,5);
				GelinkteKaartenMemory.put(5,0);
				GelinkteKaartenMemory.put(1,6);
				GelinkteKaartenMemory.put(6,1);
				GelinkteKaartenMemory.put(2,7);
				GelinkteKaartenMemory.put(7,2);
				GelinkteKaartenMemory.put(3,8);
				GelinkteKaartenMemory.put(8,3);
				GelinkteKaartenMemory.put(4,9);
				GelinkteKaartenMemory.put(9,4);
				GelinkteKaartenMemory.put(10,14);
				GelinkteKaartenMemory.put(14,10);
				GelinkteKaartenMemory.put(12,13);
				GelinkteKaartenMemory.put(13,12);
				GelinkteKaartenMemory.put(11,15);
				GelinkteKaartenMemory.put(15,11);
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		memory_mainView ticTacToe = new memory_mainView(4,125);
	}
	
	private void fillSpaces()
	{	
		 ArrayList<Integer> list = new ArrayList<Integer>();
	        for (int i=0; i<16; i++) {
	            list.add(i);
	        }
	        Collections.shuffle(list);
	        for (int i=0; i<16; i++) {
	        	spaces2[list.get(i)] = ImageArray[i];
	        }		
	}

	private class Painter extends JPanel implements MouseListener {
		private static final long serialVersionUID = 1L;

		public Painter() {
			setFocusable(true);
			requestFocus();
			setBackground(Color.WHITE);
			addMouseListener(this);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			render(g);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (accepted) {
				
				//System.out.println("Click");
				//System.out.println(yourTurn);


				if (yourTurn && !unableToCommunicateWithOpponent && !won && !enemyWon) {
					int x = e.getX() / lengthOfSpace;
					int y = e.getY() / lengthOfSpace;
					y *= groteGame;
					int position = x + y;
					
					checkforGelinkteKaarten[indexClick]= position;
					System.out.println("Click " + checkforGelinkteKaarten[indexClick]);
					
					if(indexClick == 0)
					{
						tweeZetten = false;
						indexClick = 1;
						
						try {
							dos.writeInt(position); 
							dos.flush();
							System.out.println("flush " + position);

						} catch (IOException e1) {
							errors++;
							e1.printStackTrace();
						}
					}					
					else 
					{
						//System.out.println("gelinkt 0: "+ checkforGelinkteKaarten[0]);
						//System.out.println("gelinkt 1: "+ checkforGelinkteKaarten[1]);

						tweeZetten = true;
						
						repaint();
						Toolkit.getDefaultToolkit().sync();
						
						checkMatch();

						try {
							dos.writeInt(position); 
							dos.flush();
						} catch (IOException e1) {
							errors++;
							e1.printStackTrace();
						}

						System.out.println("DATA WAS SENT");
						checkForWin();
						checkForTie();
						
						indexClick = 0;
						yourTurn = false;
						checkforGelinkteKaarten[0] = -1;
						checkforGelinkteKaarten[1] = -1;
					}					
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

	}

}
