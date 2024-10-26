/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnectionClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martaguzman
 */

/* Pensar esta clase como implementar -> ver ejemplos clase */
 public class ConnectionClient  implements Serializable {
    
    private static final long serialVersionUID = 6L;

	private Socket socket;
	private ObjectOutputStream objectOutput;
	private ObjectInputStream objectInput;

	// CONSTRUCTOR
	public ConnectionClient (String serverIP, int port) {
		try {

			this.socket = new Socket(serverIP, port);
			this.objectOutput = new ObjectOutputStream(socket.getOutputStream());
			this.objectInput = new ObjectInputStream(socket.getInputStream());

			String role = "patient"; // CON EL DOCTOR LO MISMO ??
			objectOutput.writeObject(role);
			objectOutput.flush();

		} catch (IOException ex) {
			Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
                        
		}

	}

	// CLOSING CONNECTION
	public void closeConnection() throws IOException {

		objectInput.close();
		objectOutput.close();
		socket.close();

	}
    
}
