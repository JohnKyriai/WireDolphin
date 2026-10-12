import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

public class Main{
	public static void main(String[] args){
		
		final JFrame frame = new JFrame();
		frame.setSize(800, 500);
	    frame.setTitle("WireDolphin");
		frame.setLayout(new BorderLayout());
		
		JPanel upPanel = new JPanel(new BorderLayout());
		JPanel centrePanel = new JPanel(new BorderLayout());
		
		final JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu viewMenu = new JMenu("View");
		JMenu editMenu = new JMenu("Edit");
		JMenu helpMenu = new JMenu("Help");
		
		JMenuItem refreshItem = new JMenuItem("Refresh");
		final JMenuItem loopItem = new JMenuItem("Live updates");
		JMenuItem fontItem = new JMenuItem("Font");
		
		JMenuItem copyItem = new JMenuItem("Copy Results");
		
		JMenuItem aboutItem = new JMenuItem("About WireDolphin");
		JMenuItem definitionsItem = new JMenuItem("Definitions");
		
		final JButton startButton = new JButton("Start");
		JButton stopButton  = new JButton("Stop");
		
		final javax.swing.Timer liveTimer = new javax.swing.Timer(2000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				startButton.doClick();
				
			}
		});
		
		viewMenu.add(refreshItem);
		viewMenu.add(loopItem);
		viewMenu.add(fontItem);
		
		editMenu.add(copyItem);
		
		helpMenu.add(aboutItem);
		helpMenu.add(definitionsItem);
		
		menuBar.add(viewMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		
		upPanel.add(startButton, BorderLayout.WEST);
		upPanel.add(stopButton, BorderLayout.EAST);
		
		centrePanel.add(scrollPane, BorderLayout.CENTER);
		
		frame.setJMenuBar(menuBar);
		frame.add(centrePanel, BorderLayout.CENTER);
		frame.add(upPanel, BorderLayout.NORTH);
		
		startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				startButton.setEnabled(false);
				frame.setTitle("WireDolphin - Scanning...");
				
				Thread scanThread = new Thread(new Runnable(){
					@Override
					public void run(){
						final StringBuilder output = new StringBuilder();
						
						try{
							Process process = new ProcessBuilder(
							"cmd.exe", "/c", "netstat -ano"
							).redirectErrorStream(true).start();
							
							BufferedReader reader = new BufferedReader(
							new InputStreamReader(process.getInputStream())
							);
							
							String line;
							
							while ((line = reader.readLine()) !=null){
								output.append(line).append("\n");
							}
							
							reader.close();
							process.waitFor();
							
						} catch (Exception ex) {
							output.append("Could not start netstat:\n");
							output.append(ex.getMessage());
						}
						
						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run(){
								textArea.setText(output.toString());
								textArea.setCaretPosition(0);
								frame.setTitle("WireDolphin");
								startButton.setEnabled(true);
							}
						});
					}
                });					
						
				scanThread.start();
			}
	            
		});
		
		loopItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (liveTimer.isRunning()){
					liveTimer.stop();
					loopItem.setText("Live Updates");
					
				} else {
					startButton.doClick();
					liveTimer.start();
					loopItem.setText("Stop Live Updates");
				}
			}
		});
		
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				liveTimer.stop();
				loopItem.setText("Live Updates");
			}
		});
		
		aboutItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String username = System.getProperty("user.name");
				 JOptionPane.showMessageDialog(null, "Javalator WireDolphin (c) - Licensed to "+username+", under the MIT license", "About WireDolphin", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		definitionsItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				JFrame frame2 = new JFrame();
				frame2.setTitle("Key definitions");
				frame2.setLayout(new GridLayout(0, 1, 5, 5));
				
				JLabel tcp = new JLabel("TCP establishes a live connection between the sender and receiver before any data is sent. This connection is created using a three-way handshake (SYN, SYN-ACK, ACK)");
				JLabel udp = new JLabel("User Datagram Protocol (UDP) is a lightweight, connectionless transport layer communication protocol that sends data packets (called datagrams) directly across a network without establishing a prior handshake, confirming delivery, or ordering packets.");
				JLabel listen = new JLabel("Listening (LISTEN): A server socket is open and actively waiting for an incoming connection request from a client. It is ready to accept a handshake.");
				JLabel established = new  JLabel("Established (ESTABLISHED): A active, open connection is complete. Both the client and server have finished the TCP three-way handshake, and data can now flow back and forth.");
                JLabel timewait = new JLabel("Time Wait: A socket has just been closed, and it is waiting for a brief period (twice the maximum segment lifetime) to ensure any delayed or lost packets still floating around in the network are safely discarded. This prevents old packets from interfering with a future connection on the same port.");
                
				frame2.add(tcp);
                frame2.add(udp);
                frame2.add(listen);
                frame2.add(established);
				frame2.add(timewait);
					
				frame2.pack();	
				frame2.setLocationRelativeTo(frame);
				frame2.setVisible(true);
				
			}   
			
			
		});
		
		fontItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				final JFrame frame3 = new JFrame();
				frame3.setTitle("WireDolphin - Choose Font");
				frame3.setLayout(new GridLayout(0, 1, 5, 5));
				
				final JCheckBox ft1 = new JCheckBox("Times New Roman");
				final JCheckBox ft2 = new JCheckBox("Comic Sans");
				final JCheckBox ft3 = new JCheckBox("Algerian");
				final JCheckBox ft4 = new JCheckBox("Helvetica");
				
				JButton apply = new JButton("Apply changes");
				
				apply.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						if (ft1.isSelected()){
							textArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
							
						} else if (ft2.isSelected()){
							textArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
							
						} else if (ft3.isSelected()){
							textArea.setFont(new Font("Algerian", Font.PLAIN, 16));
							
						} else if (ft4.isSelected()){
							textArea.setFont(new Font("Helvetica", Font.PLAIN, 16));
							
						} else{
							JOptionPane.showMessageDialog(null, "Choose a font mate!", "Font selection", JOptionPane.INFORMATION_MESSAGE);
							return;
							
						}
						
						frame3.dispose();
						
		
					}
				});
						
				frame3.add(ft1);
				frame3.add(ft2);
				frame3.add(ft3);
				frame3.add(ft4);
				frame3.add(apply);

				frame3.pack();	
				frame3.setLocationRelativeTo(frame);
				frame3.setVisible(true);
					
			}
		});
		
		copyItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String textToCopy = textArea.getText();
				
				StringSelection selection = new StringSelection(textToCopy);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				
				clipboard.setContents(selection, null);
			}
		});
		
		refreshItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				startButton.doClick();
			}
		});
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}