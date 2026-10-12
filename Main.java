import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

// W I R E D O L P H I N    V. 1 . 0

// WireDolphin! - very original name
// All in all, what this does is just netstat but gives it a GUI and can put it on repeat,
// that is most of its functionality so far.

// I made this in 4 hours! 
// I used AI when appropriate and only to help me identify errors and to help me with some one liners when I was stuck.
// I also used stack overflow to help me when I was stuck
// I built this on Java 7 so it has compatibility with Windows Vista and onwards, because I coded the whole thing in Windows Vista for some reason
// If you want to modify the code go for it but I wanna keep this brand so if you go and fork just give me credits and we're cool

// Don't run this on Windows XP i doubt it will work :(

// I will keep the actual comments on the code formal and I will disclose if I used AI on some lines

// This app has really limited functionality but i plan to add more to it in the future!
// Enjoy :)

public class Main{
	public static void main(String[] args){
		
		// Setup for the main window
		final JFrame frame = new JFrame(); // This makes the window
		frame.setSize(800, 500); // This sets the size for the window 
	    frame.setTitle("WireDolphin"); // This sets the name of the main Window
		frame.setLayout(new BorderLayout()); // This sets the default Layout of the main window
		// This ends here
		
		// Panel Creation
		JPanel upPanel = new JPanel(new BorderLayout());// This creates the top panel which hosts the Start and stop buttons with a Border Layout
		JPanel centrePanel = new JPanel(new BorderLayout()); // This creates the central Panel which hosts the textArea which the results of the scan are showed on
		// This ends here
		
		// This creates the text area
		final JTextArea textArea = new JTextArea(); // This is the text area which the results of the scan are displayed on
		JScrollPane scrollPane = new JScrollPane(textArea); // This adds a scroll bar because the scan usually shows a lot of results so you need to scroll through it
		textArea.setEditable(false); // This makes it so the user can not edit/write random stuff on top of the results
		// This ends here
		
		// This creates the Menu bar on top
		JMenuBar menuBar = new JMenuBar(); // This makes the main menu bar element 
		
		JMenu viewMenu = new JMenu("View"); // This is the view button on the menu bar 
		JMenu editMenu = new JMenu("Edit"); // This is the edit button on the menu bar
		JMenu helpMenu = new JMenu("Help"); // This is the help button on the menu bar
		// This ends here
		
		// This creates the items that appear on the View button on the Menu bar
		JMenuItem refreshItem = new JMenuItem("Refresh"); // This just starts a new scan
		final JMenuItem loopItem = new JMenuItem("Live updates"); // This makes it so the scan happens live and refreshes by itself so the results are constantly updating
		JMenuItem fontItem = new JMenuItem("Font"); // Just changes the font of the results in the text area
		// This ends here 
		
		// This is the sole item for the edit menu
		JMenuItem copyItem = new JMenuItem("Copy Results"); // Self explanatory
		// This ends here
		
		// This creates the items that appear on the help button on the Menu bar
		JMenuItem aboutItem = new JMenuItem("About WireDolphin"); // Just shows quick info about the app
		JMenuItem definitionsItem = new JMenuItem("Definitions"); // Shows key definitions of protocols and other things
		// This ends here
		
		// This makes the buttons 
		final JButton startButton = new JButton("Start"); // makes the start button
		JButton stopButton  = new JButton("Stop"); // makes the stop button
		
		final javax.swing.Timer liveTimer = new javax.swing.Timer(2000, new ActionListener(){ // the live timer every 2 seconds
			@Override
			public void actionPerformed(ActionEvent e){
				startButton.doClick(); // starts the start button
				
			}
		});
		// This ends here
		
		//FROM THIS POINT WE ADD ELEMENTS TO THE FRAME
		
		// Adds items to the view button of the Menu bar
		viewMenu.add(refreshItem);
		viewMenu.add(loopItem);
		viewMenu.add(fontItem);
		// This ends here
		
		// Adds the copy item to the edit button in the Menu bar
		editMenu.add(copyItem);
		// This ends here
		
		// This adds the items to the help button of the Menu bar
		helpMenu.add(aboutItem);
		helpMenu.add(definitionsItem);
		// This ends here

        // This adds the view, help and edit buttons to the Menu bar		
		menuBar.add(viewMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		// This ends here
		
		// This adds the start and stop buttons to the upmost panel
		upPanel.add(startButton, BorderLayout.WEST); // puts the start button on the west side
		upPanel.add(stopButton, BorderLayout.EAST); // puts the stop button on the east side
		// This ends here
		
		// Adds the textarea and scrollpane
		centrePanel.add(scrollPane, BorderLayout.CENTER); // Centre of the app
		// This ends here
		
		// Adds all these to the frame
		frame.setJMenuBar(menuBar); // adds the menu bar
		frame.add(centrePanel, BorderLayout.CENTER); // adds the centre panel
		frame.add(upPanel, BorderLayout.NORTH); // adds the up panel
		// This ends here
		
		// ACTION LISTENERS FROM THIS POINT ON
		
		// Executes the service
		startButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				startButton.setEnabled(false);
				frame.setTitle("WireDolphin - Scanning..."); // Shows that the app is scanning
				
				Thread scanThread = new Thread(new Runnable(){
					@Override
					public void run(){
						final StringBuilder output = new StringBuilder();
						
						try{ // This is in a try catch block because it could break 
							Process process = new ProcessBuilder(
							"cmd.exe", "/c", "netstat -ano" // the actual function the app does 
							).redirectErrorStream(true).start();
							
							BufferedReader reader = new BufferedReader( // I used AI to get these two buffered reader lines i was stuck
							new InputStreamReader(process.getInputStream()) // and this
							);
							
							String line;
							
							while ((line = reader.readLine()) !=null){
								output.append(line).append("\n");
							}
							
							reader.close();
							process.waitFor();
							
						} catch (Exception ex) {
							output.append("Could not start netstat:\n"); // in case of an error the app will gracefully recover instead of crashing
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
		// This ends here
		
		// This is for the live view
		loopItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (liveTimer.isRunning()){
					liveTimer.stop();
					loopItem.setText("Live Updates"); // If the live view is not running it displays this
					
				} else {
					startButton.doClick();
					liveTimer.start();
					loopItem.setText("Stop Live Updates"); // If they are running then it shows this
				}
			}
		});
		// This ends here
		
		// Stop button just ends the live view
		stopButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				liveTimer.stop();
				loopItem.setText("Live Updates"); // returns the button text to its default setting
			}
		});
		// This ends here
		
		// about item
		aboutItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				// Gets the users system name
				String username = System.getProperty("user.name");
				// Shows a dialogue window
				 JOptionPane.showMessageDialog(null, "Javalator WireDolphin (c) - Licensed to "+username+", under the MIT license", "About WireDolphin", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		// This ends here
		
		// Definitions item
		definitionsItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				JFrame frame2 = new JFrame();
				frame2.setTitle("Key definitions");
				frame2.setLayout(new GridLayout(0, 1, 5, 5));
				
				// Just shows the definitions of the different key words that newbies might not know (I got these defs from AI)
				JLabel tcp = new JLabel("TCP establishes a live connection between the sender and receiver before any data is sent. This connection is created using a three-way handshake (SYN, SYN-ACK, ACK)");
				JLabel udp = new JLabel("User Datagram Protocol (UDP) is a lightweight, connectionless transport layer communication protocol that sends data packets (called datagrams) directly across a network without establishing a prior handshake, confirming delivery, or ordering packets.");
				JLabel listen = new JLabel("Listening (LISTEN): A server socket is open and actively waiting for an incoming connection request from a client. It is ready to accept a handshake.");
				JLabel established = new  JLabel("Established (ESTABLISHED): A active, open connection is complete. Both the client and server have finished the TCP three-way handshake, and data can now flow back and forth.");
                JLabel timewait = new JLabel("Time Wait: A socket has just been closed, and it is waiting for a brief period (twice the maximum segment lifetime) to ensure any delayed or lost packets still floating around in the network are safely discarded. This prevents old packets from interfering with a future connection on the same port.");
                
				// adds them to the frame
				frame2.add(tcp);
                frame2.add(udp);
                frame2.add(listen);
                frame2.add(established);
				frame2.add(timewait);
					
				frame2.pack();	
				frame2.setLocationRelativeTo(frame);
				frame2.setVisible(true); // makes frame visible
				
			}   
			
			
		});
		// This ends here
		
		// font selector
		fontItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				
				final JFrame frame3 = new JFrame();
				frame3.setTitle("WireDolphin - Choose Font");
				frame3.setLayout(new GridLayout(0, 1, 5, 5)); // makes it so they are in a grid layout
				
				// Just the different fonts 
				final JCheckBox ft1 = new JCheckBox("Times New Roman");
				final JCheckBox ft2 = new JCheckBox("Comic Sans");
				final JCheckBox ft3 = new JCheckBox("Algerian");
				final JCheckBox ft4 = new JCheckBox("Helvetica");
				
				// This button does all the magic 
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
							JOptionPane.showMessageDialog(null, "Choose a font mate!", "Font selection", JOptionPane.INFORMATION_MESSAGE); // Cheeky message
							return;
							
						}
						
						frame3.dispose(); // exits the frame after selection is done and apply is hit
						
		
					}
				});
				
				// adds all the things to the frame
				frame3.add(ft1);
				frame3.add(ft2);
				frame3.add(ft3);
				frame3.add(ft4);
				frame3.add(apply);

				frame3.pack(); // java sets the size
				frame3.setLocationRelativeTo(frame);
				frame3.setVisible(true);
					
			}
		});
		// This ends here
		
		// Copies the textArea and puts it on your clipboard
		copyItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String textToCopy = textArea.getText();
				
				StringSelection selection = new StringSelection(textToCopy);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				
				clipboard.setContents(selection, null);
			}
		});
		// This ends here
		
		// just a glorified start button
		refreshItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				startButton.doClick();
			}
		});
		// This ends here
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); // makes it so you can see the app
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // and so it closes gracefully
		
	}
} // and the program is done :D