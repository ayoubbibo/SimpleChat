package simplechat1;

import simplechat1.common.ChatIF;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerUI implements ChatIF,Runnable  {
    private EchoServer server;


    public ServerUI(EchoServer server){
        this.server = server;
    }


    @Override
    public void display(String message) {
        System.out.println(message);
    }

    public void run()
    {
        try
        {
            BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while (true)
            {
                message = fromConsole.readLine();
                server.handleMessageFromServerUI(message);
            }
        }
        catch (Exception e)
        {
            System.out.println("Unexpected error while reading from console!");
        }
    }
}
