package websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;

@ServerEndpoint("/chat/{userId}")
public class ChatEndpoint {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final Map<String, String> userNames = new ConcurrentHashMap<>();
    private static final Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());
    private static final Set<String> onlineAdmins = Collections.synchronizedSet(new HashSet<>());
    private static final Map<String, List<String>> chatHistories = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        sessions.put(userId, session);
        chatHistories.putIfAbsent(userId, new ArrayList<>());

        if (userId.startsWith("admin_")) {
            onlineAdmins.add(userId);
            broadcastOnlineUsers();
        } else {
            onlineUsers.add(userId);
            broadcastOnlineUsers();
            sendChatHistory(session, userId);
        }
    }

    @OnMessage
    public void onMessage(String message, @PathParam("userId") String userId) {
        try {
            if (message.startsWith("username:")) {
                String username = message.substring(9);
                userNames.put(userId, username);
                broadcastOnlineUsers();
                return;
            }

            // Xử lý yêu cầu lịch sử từ admin
            if (message.startsWith("history:") && userId.startsWith("admin_")) {
                String targetUserId = message.substring(8);
                sendChatHistory(sessions.get(userId), targetUserId);
                return;
            }

            String[] parts = message.split(":", 2);
            if (parts.length == 2) {
                String toUserId = parts[0];
                String content = parts[1];
                String senderName = userNames.getOrDefault(userId, userId);
                String formattedMessage = senderName + ": " + content;

                if (userId.startsWith("admin_")) {
                    Session toSession = sessions.get(toUserId);
                    if (toSession != null && toSession.isOpen()) {
                        toSession.getBasicRemote().sendText(formattedMessage);
                        chatHistories.get(toUserId).add(formattedMessage);
                    }
                } else {
                    for (String adminId : onlineAdmins) {
                        Session adminSession = sessions.get(adminId);
                        if (adminSession != null && adminSession.isOpen()) {
                            adminSession.getBasicRemote().sendText(formattedMessage);
                            chatHistories.get(userId).add(formattedMessage);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") String userId) {
        sessions.remove(userId);
        userNames.remove(userId);
        if (userId.startsWith("admin_")) {
            onlineAdmins.remove(userId);
        } else {
            onlineUsers.remove(userId);
        }
        broadcastOnlineUsers();
    }

    @OnError
    public void onError(Session session, @PathParam("userId") String userId, Throwable throwable) {
        sessions.remove(userId);
        userNames.remove(userId);
        if (userId.startsWith("admin_")) {
            onlineAdmins.remove(userId);
        } else {
            onlineUsers.remove(userId);
        }
        broadcastOnlineUsers();
    }

    private void broadcastOnlineUsers() {
        Map<String, String> onlineUsersWithNames = new HashMap<>();
        for (String userId : onlineUsers) {
            onlineUsersWithNames.put(userId, userNames.getOrDefault(userId, "User " + userId));
        }
        String onlineUsersJson = new Gson().toJson(onlineUsersWithNames);
        sessions.forEach((uId, session) -> {
            try {
                if (uId.startsWith("admin_") && session.isOpen()) {
                    session.getBasicRemote().sendText("online:" + onlineUsersJson);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendChatHistory(Session session, String userId) {
        try {
            if (session != null && session.isOpen() && chatHistories.containsKey(userId)) {
                List<String> history = chatHistories.get(userId);
                for (String msg : history) {
                    session.getBasicRemote().sendText(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}