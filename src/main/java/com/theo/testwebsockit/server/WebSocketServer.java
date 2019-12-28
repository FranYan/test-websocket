package com.theo.testwebsockit.server;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer
{

        private static int onlineCount = 0;

        private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

        private Session session;

        private String sid =  "";

        @OnOpen
        public void onOpen(Session session,@PathParam("sid")String sid)
        {
                this.session = session;
                webSocketSet.add(this);

                this.sid = sid;
                try
                {
                        sendMessage("连接成功！");
                }catch (Exception e){
                        e.printStackTrace();
                }
        }
        @OnClose
        public void onClose(){
                webSocketSet.remove(this);
        }

        @OnMessage
        public void onMessage(String message,Session session)
        {
                for (WebSocketServer item : webSocketSet){

                        try
                        {
                                item.sendMessage(message);
                        }catch (Exception e){
                                e.printStackTrace();
                        }

                }
        }

        @OnError
        public void onError(Session session,Throwable throwable)
        {
                throwable.printStackTrace();
        }

        public void sendMessage(String message) throws IOException
        {
                this.session.getBasicRemote().sendText(message);
        }

        public static void sendInfo(String message,@PathParam("sid")String sid) throws IOException
        {
                for (WebSocketServer item : webSocketSet)
                {
                        try
                        {
                                if (sid == null){
                                        item.sendMessage(message);
                                }else if (item.sid.equals(sid)){
                                        item.sendMessage(message);
                                }
                        }catch (IOException e){
                                continue;
                        }
                }
        }

        public static synchronized int getOnlineCount()
        {
                return onlineCount;
        }

        public static synchronized void setOnlineCount(int onlineCount)
        {
                WebSocketServer.onlineCount = onlineCount;
        }

        public static synchronized void subOnlineCount() {
                WebSocketServer.onlineCount--;
        }
}
