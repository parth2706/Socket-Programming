// Student Name: Parth B. Mehta
// Student Id: 1001668756
// Basic part or framework of source code is taken from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
package com.gfg.multithreaded.socketprog;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

public class Client
{
    final static int ServerPort = 43211;
    private static int clientCount=0;
    private  JFrame frame;
    JButton btnNewButton;
    JTextPane textPane;
    JTextPane textPane_1;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    private static String hostname;
    private static JSpinner spinner;
    
    private JRadioButton rdbtnNewRadioButton_1;
    private JRadioButton rdbtnNewRadioButton_2;
    private JRadioButton rdbtnNewRadioButton_3;
    static public JTextArea textArea;
    
    private static JFrame frame1;
	private static JTextField textField1;
    
    public Client() {
    	initialize();
    }
    //Below code establishes the GUI for Client
    private void initialize() {
		frame = new JFrame("");
		//frame.setTitle("Parth");
		frame.setBounds(100, 100, 812, 570);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		 textPane = new JTextPane();
		textPane.setBounds(30, 50, 725, 125);
		frame.getContentPane().add(textPane);
		
		 btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {         // An on-click listener for Send Button on Client GUI
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"Message sent");
				StringBuffer message =new StringBuffer(textPane.getText());
				JOptionPane.showMessageDialog(null,message);
				try {
                    // write on the output stream
					// Start
					String httpRequestMaker = Client.httpRequestMaker("POST",hostname, message.toString());
					//String encoded_value = URLEncoder.encode(new String(httpRequestMaker), "UTF-8");
					// End
              //  	String encoded_value = URLEncoder.encode(new String(message), "UTF-8");  *** Commented this line
                	
                    dos.writeUTF(httpRequestMaker);
                } catch (IOException e) {
                    e.printStackTrace();
                }
				
			}
		});
		btnNewButton.setBounds(222, 270, 115, 29);
		frame.getContentPane().add(btnNewButton);
		
		
		
		JLabel lblTypeYourMessage = new JLabel("Type your message below");
		lblTypeYourMessage.setBounds(30, 14, 252, 20);
		frame.getContentPane().add(lblTypeYourMessage);
		
//		rdbtnNewRadioButton_1 = new JRadioButton("Client 1");
//		rdbtnNewRadioButton_1.setBounds(56, 187, 155, 29);
//		frame.getContentPane().add(rdbtnNewRadioButton_1);
//		
//		rdbtnNewRadioButton_2 = new JRadioButton("Client 2");
//		rdbtnNewRadioButton_2.setBounds(337, 187, 155, 29);
//		frame.getContentPane().add(rdbtnNewRadioButton_2);
//		
//		rdbtnNewRadioButton_3 = new JRadioButton("Both");
//		rdbtnNewRadioButton_3.setBounds(600, 187, 155, 29);
//		frame.getContentPane().add(rdbtnNewRadioButton_3);
//		
//		ButtonGroup rdbtnNewRadioButton_grp = new ButtonGroup();
//		rdbtnNewRadioButton_grp.add(rdbtnNewRadioButton_1);
//		rdbtnNewRadioButton_grp.add(rdbtnNewRadioButton_2);
//		rdbtnNewRadioButton_grp.add(rdbtnNewRadioButton_3);
		
		JButton button = new JButton("Close");
		button.addActionListener(new ActionListener() {     // An on-click listener function on Close button to properly logout
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null,"Do you want to close the window?");
				try {
                    // write on the output stream
					// Start
					String httpRequestMaker = Client.httpRequestMaker("POST",hostname, "logout");
				//	String encoded_value = URLEncoder.encode(new String(httpRequestMaker), "UTF-8");
					// End
              //  	String encoded_value = URLEncoder.encode(new String(message), "UTF-8");  *** Commented this line
                	
                    dos.writeUTF(httpRequestMaker);
                   // frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    frame.setVisible(false);
                    frame.dispose();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			}
			
		});
		button.setBounds(473, 270, 115, 29);
		frame.getContentPane().add(button);
		
		
		
		JLabel lblMessageReceived = new JLabel("Message received :");
		lblMessageReceived.setBounds(30, 304, 181, 20);
		frame.getContentPane().add(lblMessageReceived);
		
		
		
	    textArea = new JTextArea();
		textArea.setBounds(30, 340, 713, 125);
		frame.getContentPane().add(textArea);
		
//		spinner = new JSpinner();
//		spinner.setBounds(131, 228, 145, 26);
//		frame.getContentPane().add(spinner);
//		
//		JLabel lblSendTo = new JLabel("Send To:");
//		lblSendTo.setBounds(37, 234, 79, 20);
//		frame.getContentPane().add(lblSendTo);
		
	}
    
//    public static void setName(String s)
//    {
//    	frame.setName(s);
//    }
    // An httpRequest is made using this function and thus called when sending a request to server
    public static String httpRequestMaker(String method1,String useragent,String message)
    {
    	String method= method1; //  GET or POST
    	String host= "Host: /127.0.0.1,port="+ServerPort;
    	String user_agent= "User-Agent: "+ useragent;
    	String content_type="Content-Type: text";
    	String content_length= "Content-Length: "+ message.length();
    	String m = "Message: "+message;
    	Date d= new Date();
    	String date= "Date: "+d.toString();
    	String delimit="\r\n";
    	String request = method+" / HTTP/1.1\r\n"+host+delimit+
    			                                  user_agent+delimit+
    			                                  content_type+delimit+
    			                                  content_length+delimit+
    			                                  m+delimit+
    			                                  date;
    			         
    	return request;
    }
    // The below method is not used at all
    public static void buildNameGUI()
    {
    	frame1 = new JFrame();
		frame1.setBounds(100, 100, 450, 300);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.getContentPane().setLayout(null);
		
		JLabel lblPleaseProvideA = new JLabel("Please provide your name as client name");
		lblPleaseProvideA.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPleaseProvideA.setBounds(50, 16, 298, 20);
		frame1.getContentPane().add(lblPleaseProvideA);
		
		textField1 = new JTextField();
		textField1.setBounds(88, 52, 196, 26);
		frame1.getContentPane().add(textField1);
		textField1.setColumns(10);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
                    // write on the output stream
					// Start
					String hostname = textField1.getText();
					if(hostname != null && !hostname.contentEquals(""))
					{	String httpRequestMaker = Client.httpRequestMaker("POST",hostname, "Hostname: "+hostname);
					//String encoded_value = URLEncoder.encode(new String(httpRequestMaker), "UTF-8");
					// End
              //  	String encoded_value = URLEncoder.encode(new String(message), "UTF-8");  *** Commented this line
                
                    dos.writeUTF(httpRequestMaker);  }
				} catch (IOException e) {
                    e.printStackTrace();
                }
				
				
			}
		});
		btnNewButton.setBounds(129, 104, 115, 29);
		frame1.getContentPane().add(btnNewButton);
    }
    
    
    public static void main(String args[]) throws UnknownHostException, IOException
    {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
					System.out.println("Enter your name as client name:"); // Here the client name is asked from Console window
					// --------------------
					
//					EventQueue.invokeLater(new Runnable() {
//						public void run() {
//							try {
//								
//								Client window = new Client();
//								window.frame.setVisible(true);
//								buildNameGUI();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					});
					
					// ----------------------
					Scanner scn = new Scanner(System.in);
			        hostname = scn.nextLine();
			        window.frame.setTitle(hostname);
			        System.out.println("This is my hostname: "+hostname);
			       // System.out.println(dos.);
			        // String encoded_value = URLEncoder.encode(new String("Hostname="+hostname), "UTF-8");
			         String httpRequestMaker = Client.httpRequestMaker("POST",hostname, "Hostname: "+hostname);
			         
			         dos.writeUTF(httpRequestMaker);
			      //  window.frame.setTitle("client "+clientCount); // --> this logic is wrong, it is always appending 0 after every run.
					//clientCount++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
//        Scanner scn = new Scanner(System.in);
//        System.out.println("Enter your name as client name:"); 
//        hostname = scn.nextLine();
//        frame.setTitle("client "+clientCount)
        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);  // A socket connection request to server is made and achieved here

        // obtaining input and out streams
         dis = new DataInputStream(s.getInputStream());
         dos = new DataOutputStream(s.getOutputStream());
         System.out.println(Server.ar.size() + "Size from client class");
         // *** Here send our brand new hostname to server by using its dos object.
         System.out.println("dis object: "+dis);
        // sendMessage thread
         /*        Thread sendMessage = new Thread(new Client().new WriteThread());
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();
                    btnNewButton.addActionListener(new ActionListener() {
            			public void actionPerformed(ActionEvent arg0) {
            				JOptionPane.showMessageDialog(null,"Message sent");
            				StringBuffer message =new StringBuffer(textPane.getText());
            			}
            		});
                    try {
                        // write on the output stream
                    	String encoded_value = URLEncoder.encode(msg, "UTF-8");
                        dos.writeUTF(encoded_value);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
  */
             // readMessage thread
       // Thread readMessage = new Thread(new Client().new ReadThread());
         // readMessage method will read the response from Server
         Thread readMessage = new Thread(new Runnable()  
         { 
             @Override
             public void run() { 
   
                 while (true) { 
                	 try {
                         // read the message sent to this client
                		// if(dis.)
          //  dis.available() > 0 dis.readUTF() != null &&            
                		 if(dis.available() > 0 )   
                       {	    
                		 String msg = dis.readUTF();
                      //   String decoded_value = URLDecoder.decode(msg, "UTF-8");
                         System.out.println("Client Listening..."+msg);
                        // textPane_1.setText(decoded_value);
//                        int equal_to_index= decoded_value.indexOf("=");
//                         String hostname_label = decoded_value.substring(0,equal_to_index-1);
//                         if(hostname_label.equals("Hostname"))
//                         {
//                        	 setName(decoded_value.substring(equal_to_index+1));
//                        	 
//                         }
//                         else {
                        textArea.append(msg+"\n");
//                         this.window.frame.setName("client "+clientCount);
     					//clientCount++;
                      //   }
                        // textArea.append("Samir");
                         //lblHh.setText(decoded_value);
                        // lblHh.set
                	 }  
                	 } catch (Exception e) {

                         e.printStackTrace();
                     } 
                 } 
             } 
         }); 
         
         readMessage.start();
    /*    {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        String decoded_value = URLDecoder.decode(msg, "UTF-8");
                        System.out.println(decoded_value);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });
  */
       // sendMessage.start();
         // Below thread is not used -- Kindly ignore
        Thread getClients = new Thread(new Runnable()
        		{

					@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println("Hi Hi HI");
						int no_of_active_clients=0;
						String names[]= new String[]{};
						while(true)
						{    
							//if(no_of_active_clients != Server.ar.size())
							//{    
								//System.out.println(Server.ar.size());
							synchronized(Server.ar)
							{
									if(Server.ar.size() == 0 || no_of_active_clients == Server.ar.size())
								{
									System.out.println(Server.ar.size());
									try {
										Server.ar.wait();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
									no_of_active_clients = Server.ar.size();
								   for( int i=0; i<no_of_active_clients; i++)
								   {
									   ClientHandler cd= Server.ar.get(i);
									   names[i] = cd.getHostName();
									   System.out.println(names[i]);
									 //  textArea.append(name+"\n");
								   }
								   if(names.length != 0)
								   { SpinnerModel model = new SpinnerListModel(names);
								//   for(String name: names)
								//   {
								//	   System.out.println(names);
								//   }
								   spinner.setModel(model); }
								//}
								   
							}
						}
					}
        	
        		
        		});
        System.out.println("Before clients thread start");
        getClients.start();
        

    }

    
    
 // Below thread is not used -- Kindly ignore
	class WriteThread implements Runnable {
		@Override
        public void run() {
            while (true) {

                // read the message to deliver.
             //   String msg = scn.nextLine();
                btnNewButton.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent arg0) {
        				JOptionPane.showMessageDialog(null,"Message sent");
        				StringBuffer message =new StringBuffer(textPane.getText());
        				JOptionPane.showMessageDialog(null,message);
        				try {
                            // write on the output stream
                        	String encoded_value = URLEncoder.encode(new String(message), "UTF-8");
                            dos.writeUTF(encoded_value);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
        			}
        		});
                
            }
        }
	}
	// Below thread is not used -- Kindly ignore
	class ReadThread implements Runnable {
		@Override
        public void run() {
           

                // read the message to deliver.
             //   String msg = scn.nextLine();
            	 while (true) {
                     try {
                         // read the message sent to this client
                         String msg = dis.readUTF();
                         String decoded_value = URLDecoder.decode(msg, "UTF-8");
                         System.out.println(decoded_value);
                         textPane_1.setText(decoded_value);
                         textArea.append(decoded_value);
                         textArea.append("Samir");
                         //lblHh.setText(decoded_value);
                        // lblHh.set
                     } catch (IOException e) {

                         e.printStackTrace();
                     }
                 }
                
            
        }
	}
	
}