// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package simplechat1.client;

import simplechat1.ocsf.client.*;
import simplechat1.common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  public static final String END = "\u001B[0m";
  public static final String BLACK = "\u001B[30m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";

  public static final String BLUE = "\u001B[34m";
  public static final String PURPLE = "\u001B[35m";
  public static final String CYAN = "\u001B[36m";
  public static final String WHITE = "\u001B[37m";
  private final String id;
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI, String id)
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.id = id;
    //openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  @Override
  protected void connectionClosed() {
    clientUI.display("the connexion is Closed");
  }

  @Override
  protected void connectionException(Exception e) {
    clientUI.display("Brutal stop of the server" + e);
    quit();
  }
@Override
  protected void connectionEstablished()
{
  clientUI.display("connection established");
}
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message){

    if(!message.startsWith("#"))
    {
      try {
        sendToServer(message);
      } catch (IOException e) {
        clientUI.display
                (RED + "Could not send message to server.  Terminating client." + END);
        quit();
      }
    } else {
      String[] cmd = message.split(" ");
      switch (cmd[0]) {
        case "#quit":
          try {
            sendToServer("#logoff");
          } catch (IOException e) {
            clientUI.display(RED + "Could not send message to server. probably disconnected" + e + END);
          } finally {
            clientUI.display(RED + "Terminating client." + END);
            quit();
          }
          break;
        case "#logoff":
          try {
            sendToServer(cmd[0]);
            closeConnection();
          } catch (IOException e) {
            clientUI.display(RED + "Could not send message to server. probably disconnected" + e +END);
          } finally {
            clientUI.display(RED + "You are disconnected from the server" + END);
          }
          break;
        case "#sethost":
           setHost(cmd[1]);
           clientUI.display(GREEN + "host set to " + getHost() + END);
          break;
        case "#setport":
          setPort(Integer.parseInt(cmd[1]));
          clientUI.display(GREEN + "Port set to " + getPort() + END);
          try{
            sendToServer("#logoff");
            closeConnection();
            openConnection();
          } catch (IOException e)
          {
            clientUI.display("error on the set port " + e.getMessage());
          }
          break;
        case "#login":
          if(isConnected())
          {
            clientUI.display(YELLOW + "Client already connected !" + END);
          }else {
            try{
              openConnection();
              if(cmd[1] == "")
              {
                sendToServer("#login " + this.id);
              } else
              {
                sendToServer("#login " + cmd[1]);
              }
            } catch (IOException e)
            {
              clientUI.display(RED + "Cannot login server prompt !" + END);
              quit();
            }
          }
          break;
        case "#gethost":
          clientUI.display(BLUE + "the Host is -> " + getHost() + END);
          break;
        case "#getport":
          clientUI.display(BLUE + "the Port is -> " + getPort() + END);
          break;
        default:
          clientUI.display(YELLOW + "thi commande dont exist ! " + END);
          break;
      }
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

//End of ChatClient class
}