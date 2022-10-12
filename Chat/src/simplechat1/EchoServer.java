package simplechat1;// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import simplechat1.common.ChatIF;
import simplechat1.ocsf.server.*;

import java.io.IOException;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{


  public static final String END = "\u001B[0m";
  public static final String BLACK = "\u001B[30m";
  public static final String RED = "\u001B[31m";
  public static final String GREEN = "\u001B[32m";
  public static final String YELLOW = "\u001B[33m";

  public static final String BLUE = "\u001B[34m";
  public static final String PURPLE = "\u001B[35m";
  public static final String CYAN = "\u001B[36m";
  public static final String WHITE = "\u001B[37m";

  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 3002;
  private ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF ui)
  {
    super(port);
    serverUI = ui;
    Thread serverConsole = new Thread((Runnable) serverUI);
    serverConsole.start();
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    if(!msg.toString().startsWith("#"))
    {
      serverUI.display("Message received: " + msg + " from " + client);
      this.sendToAllClients(msg);
    } else
    {
      if(client.getInfo("id") != null)
      {
        try{
          client.sendToClient("already connected with the id " + client.getInfo("id"));
        } catch (IOException e)
        {
          serverUI.display(RED + " erro on the log client in handlemessagefromcliet " + e + END);
        }
      } else {
        client.setInfo("id",msg.toString().split(" ")[1]);
        serverUI.display((String) client.getInfo("id"));
      }
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  @Override
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  @Override
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }

  @Override
  protected void serverClosed() {
    serverUI.display
            ("Server Closed.");

  }



  @Override
  synchronized protected void clientException(
          ConnectionToClient client, Throwable exception) {
    System.out.println("le client " + client + " vient de se déconnecter " + exception);
  }
  @Override
  protected void clientConnected(ConnectionToClient client)
  {
    System.out.println("le client " + client + " vient de se connecter");
  }



  /**  Handle the message wrote by the User  **/
  public void handleMessageFromServerUI(String msg){
    if(!msg.toString().startsWith("#"))
    {
      sendToAllClients("Serveur MSG>"+ msg);
    } else {
      String[] cmd = msg.split(" ");
      switch (cmd[0]){
        case "#quit":
          try{
            close();
            System.exit(0);
          } catch (IOException e){
            serverUI.display(RED + "error from the close method on the quit case " + e + END);
          }
          break;
        case "#stop":
          stopListening();
          break;
        case "#close":
          try{
            close();
           } catch (IOException e){
            serverUI.display(RED + "error from the close method " + e + END);
          }
        break;
        case "#setport":
          setPort(Integer.parseInt(cmd[1]));
          try{
            close();
            listen();
          } catch (IOException e)
          {
            serverUI.display(RED + "error in the setPort case " + cmd[1] + END);
          }
          serverUI.display(GREEN + "the port is set to " + cmd[1] + END);
          break;
        case "#getport":
          serverUI.display(CYAN + "the port is " + getPort() + END);
          break;
        case "#start":
          try{
            listen();
          } catch (IOException e)
          {
            serverUI.display(RED + "error from the start of the listen method " + e + END);
          }
          break;
        case "#client":
          server.display("Le client nommé "+clie)
          break;
        default:
          serverUI.display(YELLOW + "this commande is not handled by the server ! " + END);
          break;
      }
    }
  }



  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args The port number to listen on.  Defaults to 3002
   *          if no argument is entered.
   */
  //Class methods ***************************************************


  public static void main(String[] args) 
  {
    int port = 3002; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	ServerConsole sv = new ServerConsole(port);
  }
}
//End of EchoServer class
