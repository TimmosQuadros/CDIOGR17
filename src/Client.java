import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private Scanner input;
	private PrintWriter writer;
	private String response;
	private Socket socket;
	private static Client client;
	
	private Client(String serverIP) {
		try {
			int port = 4445;
			String address = serverIP;
			InetAddress serverAddress;
			serverAddress = InetAddress.getByName(address);
			socket = new Socket(serverAddress, port);
			input = new Scanner(socket.getInputStream());
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Client getClient(String serverIP) {
		if(client==null) {
			client = new Client(serverIP);
		}
		return client;
	}
	
	public void sendMessage(final String message) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				writer.println(message);
			}
			
		});
		thread.start();
	}
	
	public String receiveMessage() {
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!input.hasNextLine()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				response = input.nextLine();
			}
		});
		thread1.start();
		try {
			thread1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
