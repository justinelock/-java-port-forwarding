package cn.org.hentai.server.protocol.commander;

import cn.org.hentai.server.model.Host;
import cn.org.hentai.server.util.Configs;
import cn.org.hentai.server.util.Log;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Expect on 2018/1/25.
 * 指令服务，负责通知主机开始端口转发等
 */
public class CommandServer implements Runnable
{
    public void run()
    {
        int listenPort;
        ServerSocket server = null;
        try
        {
            listenPort = Configs.getInt("server.command.port", 1212);
            server = new ServerSocket(listenPort, 1000, InetAddress.getByName("0.0.0.0"));
            Log.debug("Command Server started on: " + listenPort);
            while (true)
            {
                Socket client = server.accept();
                CommandSession session = new CommandSession(client);
                new Thread(session).start();
            }
        }
        catch(Exception e)
        {
            Log.error(e);
        }
    }
}
