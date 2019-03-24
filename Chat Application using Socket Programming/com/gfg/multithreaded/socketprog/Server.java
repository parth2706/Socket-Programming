// Student Name: Parth B. Mehta
// Student Id: 1001668756
// Basic part or framework of source code is taken from https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
package com.gfg.multithreaded.socketprog;

import java.awt.EventQueue;
import java.awt.TextArea;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import java.net.*;

// Server class
public class Server
{

    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();

    // counter for clients
    static int i = 0;
    private JFrame frame;
    protected static JTextArea textArea; // Client Activity
    protected static JTextArea textArea_1; // Active Clients  JTextArea 
    protected static JTextArea textArea_2;  // HTTP message received
    protected static JTextArea textArea_3;  // HTTP message sent
    
    public Server() {
		initialize();
	}
    
    //Below method establishes the GUI screen
    private void initialize() {
    	frame = new JFrame("Server");
		frame.setBounds(100, 100, 1000, 850);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(15, 51, 349, 125);
		frame.getContentPane().add(textArea);
		
		textArea_1 = new JTextArea();
		textArea_1.setBounds(409, 51, 357, 125);
		frame.getContentPane().add(textArea_1);
		
		JLabel lblClientActivity = new JLabel("Client Activity:");
		lblClientActivity.setBounds(29, 28, 181, 20);
		frame.getContentPane().add(lblClientActivity);
		
		JLabel lblActiveClients = new JLabel("Active Clients:");
		lblActiveClients.setBounds(411, 28, 181, 20);
		frame.getContentPane().add(lblActiveClients);
		
		JLabel lblUnparsedHttpMessage = new JLabel("Unparsed HTTP Message Received:");
		lblUnparsedHttpMessage.setBounds(29, 216, 364, 20);
		frame.getContentPane().add(lblUnparsedHttpMessage);
		
		textArea_3 = new JTextArea(); // textArea_3
		textArea_3.setBounds(15, 652, 851, 118);
		frame.getContentPane().add(textArea_3);
		
//		JLabel lblUnparsedHttpMessage_1 = new JLabel("Unparsed HTTP Message Sent:");
//		lblUnparsedHttpMessage_1.setBounds(29, 800, 364, 20);
//		frame.getContentPane().add(lblUnparsedHttpMessage_1);
		
		textArea_2 = new JTextArea();
		textArea_2.setBounds(15, 238, 861, 348);
		frame.getContentPane().add(textArea_2);
		
		JLabel lblUnparsedHttpMessage_2 = new JLabel("Unparsed HTTP Message Sent:");
		lblUnparsedHttpMessage_2.setBounds(15, 616, 364, 20);
		frame.getContentPane().add(lblUnparsedHttpMessage_2);
	}
    
    
    
    public static void main(String[] args) throws IOException
    {
        // server is listening on port 43211
        ServerSocket ss = new ServerSocket(43211);

        Socket s; // This will be the other socket to do communication
        
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

        // running infinite loop for getting
        // client request
        while (true)
        {
            // Accept the incoming request
            s = ss.accept();     // Waiting to accept requests of new users

            System.out.println("New client request received : " + s);

            // obtain input and output streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

           // while(true)
           // {
            String received = dis.readUTF();   // Reading input from Clients
          //  Server.textArea_2.append(received+"\n");
            // Read change the hostname from client.
            
           // System.out.println("received String" + received);
          //  String decoded_value = URLDecoder.decode(received, "UTF-8");
            Server.textArea_2.append(received+"\n");
            //System.out.println("deceoded revceived String"+ decoded_value);	
            //}
            
            //Below code is parsing the incoming request from Sender so as to read the exact message
            StringTokenizer r= new StringTokenizer(received,"\r\n");
            //    System.out.println("Tokens start");

                r.nextToken();
                r.nextToken();
                r.nextToken();
                r.nextToken();
                r.nextToken();
                
                String message_actual= r.nextToken();
               
            String[] hostname_array = message_actual.split(":");
            System.out.println("Creating a new handler for this client..." + hostname_array[2]);
            
            // Create a new handler object for handling this request.
            ClientHandler mtch = new ClientHandler(s,hostname_array[2], dis, dos);   // sending the user-defined hostname into Client Handler object.
            
          //  String encoded_value = URLEncoder.encode(new String("Hostname="+"client " + i), "UTF-8");
           // dos.writeUTF(encoded_value);

            // Create a new Thread with this object.
            Thread t = new Thread(mtch);

            System.out.println("Adding this client to active client list");
            textArea.append(hostname_array[2]+" logged in."+"\n");             // Registering the Client Activity
            // add this client to active clients list
           
            synchronized(ar)
            {
            ar.add(mtch);
            ar.notify();
            }
            System.out.println("Size of Client handler vector list"+Server.ar.size());
            textArea_1.append(hostname_array[2]+"\n");                       // Adding the new Host in the Active list of Clients
            // *** Here get name from this mtch object and append it on a textarea for Server GUI.
            
            // start the thread.
            t.start();

            // increment i for new client.
            // i is used for naming only, and can be replaced
            // by any naming scheme
            i++;

        }
    }
}

//class Temp extends Server{
//	
//}
// ClientHandler class
class ClientHandler implements Runnable
{
    Scanner scn = new Scanner(System.in);
    private String name;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket s, String name,
                         DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;	
    }
    
    public String getHostName()
    {
    	return name;
    }

    @Override
    public void run() {

        String received;
        while (true)
        {
            try
            {
                // receive the string
                received = dis.readUTF();    // A thread for every client is continuously running to accept requests

                // Read change the hostname from client.
                
                System.out.println(received);
                //Server.textArea_2.append(received+"\n");
                String decoded_value = URLDecoder.decode(received, "UTF-8");
                Server.textArea_2.append(decoded_value+"\n");
                System.out.println("This is the decoded string "+decoded_value);
                // Start decoding logic
//                String delimit="/r/n";
//                String[] requestSplit = decoded_value.split(delimit);
//                for (String parth: requestSplit)
//                {
//                	System.out.println(parth+ "Separate values");
//                }
                // Below is the unparsing logic for every incoming request from client
                StringTokenizer r= new StringTokenizer(decoded_value,"\r\n");
           //    System.out.println("Tokens start");

               r.nextToken();
               r.nextToken();
               r.nextToken();
               r.nextToken();
               r.nextToken();
               
               String message_actual= r.nextToken();
               String dateTime = r.nextToken();
               String [] p = dateTime.split(":");
       		   String dateTime_actual = p[1].trim();
               
       		   
               
               
             //   System.out.println("Tokens ends");
                
//                String message_actual="";
//                String hostname_actual="";
//                for(String a: requestSplit)
//                {
//                	System.out.println("First");
//                	System.out.println(a);
//                	if(a.indexOf("User-Agent") > 0)
//                	{
//                		System.out.println("Second");
//                		System.out.println(a+ " here is the culprit" );
//                		String[] p =a.split(":");
//                		hostname_actual = p[1].trim();
//                	}
//                    if(a.indexOf("Message") > 0)
//                	{
//                		System.out.println("Third");
//                		System.out.println(a);
//                		String [] p =a.split(":");
//                		message_actual = p[1].trim();
//                	}
//                }
                //End decoding logic
                // Below is the code when clients logs out. Here, a "logout" is hardcoded when user clicks on Close Button.
                if(message_actual.indexOf("logout") > 0){
//                	 String [] p = message_actual.split(":");
//             		   String dateTime_actual = p[1].trim();
                	this.isloggedin=false;
                    this.s.close();
                    this.dos.close();
                    this.dis.close();
                    Server.textArea.append(this.name+" logged out."+"\n");
                    StringBuffer sb=new StringBuffer();
                    for(ClientHandler ch:Server.ar )
                    {
                    	if(ch.isloggedin)
                    	{
                    		sb.append(ch.name+"\n");
                    	}
                    }
                    Server.textArea_1.setText(sb.toString());
                    
                    break;
                }
              //  System.out.println("Actual decoded final message: " + message_actual);
                System.out.println("Actual decoded final message: " + message_actual);
                // break the string into message and recipient part
               // StringTokenizer st = new StringTokenizer(decoded_value, "#");
                StringTokenizer st = new StringTokenizer(message_actual, "#");
                String MsgToSend = st.nextToken().trim();
                System.out.println("MsgToSend"+MsgToSend);
                String recipient = st.nextToken().trim();
                System.out.println("recipient"+recipient);
                // search for the recipient in the connected devices list.
                // ar is the vector storing client of active users
                System.out.println("Step one");
                // Below is the code when a Broadcast is to be sent to "everyone"
                if(recipient.equalsIgnoreCase("everyone"))
                {
                	String message= dateTime_actual+" By "+this.name+" : "+MsgToSend.split(":")[1]+" (Sent to all)";
                	System.out.println(message + "Final message to send to client.......");
                	
                	for(ClientHandler mc: Server.ar)
                	{
                		if(mc.isloggedin==true)
                		mc.dos.writeUTF(message);
                	}
                	
                }
                System.out.println("Step two");
                // Below code will send messages to individual client to whom it is destined
                for (ClientHandler mc : Server.ar)
                {
                    // if the recipient is found, write on its
                    // output stream
                	System.out.println("Step three - inside "+"mc.name: "+mc.name+" ,recipient name: "+recipient+ " ,looged in: "+mc.isloggedin+" condition:"+(mc.name.equals(recipient) && mc.isloggedin==true));
                    if (mc.name.trim().equals(recipient.trim()) && mc.isloggedin==true)
                    {
                    	String message= dateTime_actual+" By "+this.name+" : "+MsgToSend.split(":")[1]+" (Sent to you)";
                    	System.out.println(message + "Final message to send to client.......");
                    	mc.dos.writeUTF(message);
                       Server.textArea_3.append(message+"\n");
                        break;
                    }
                    
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}


   

