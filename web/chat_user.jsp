<%-- 
    Document   : chat_user
    Created on : 24 thg 3, 2025, 00:54:38
    Author      LTB
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Chat</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 0;
        }
        .container {
            display: flex;
            height: 100vh;
            max-width: 1200px;
            margin: 0 auto;
        }
        #user-list {
            width: 300px;
            background-color: #fff;
            border-right: 1px solid #eee;
            padding: 10px;
            overflow-y: auto;
        }
        #user-select {
            width: 100%;
            border: none;
            background-color: #f0f2f5;
            padding: 10px;
            border-radius: 10px;
            outline: none;
        }
        #user-select option {
            padding: 10px;
            cursor: pointer;
        }
        #user-select option:hover {
            background-color: #e9ecef;
        }
        .chat-area {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }
        #chat-box {
            flex-grow: 1;
            height: 80vh;
            overflow-y: auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
        }
        #chat-box p {
            margin: 5px 0;
            padding: 10px 15px;
            border-radius: 18px;
            max-width: 60%;
            word-wrap: break-word;
        }
        #chat-box p:nth-child(odd) { /* Tin nhắn từ người khác */
            background-color: #e9ecef;
            color: #000;
            align-self: flex-start;
        }
        #chat-box p:nth-child(even) { /* Tin nhắn của bạn */
            background-color: #0084ff;
            color: #fff;
            margin-left: auto;
        }
        .input-area {
            display: flex;
            padding: 10px;
            background-color: #fff;
            border-top: 1px solid #eee;
        }
        #message-input {
            flex-grow: 1;
            padding: 10px;
            border: none;
            border-radius: 20px;
            background-color: #f0f2f5;
            margin-right: 10px;
            outline: none;
        }
        button {
            padding: 10px 20px;
            border: none;
            background-color: #0084ff;
            color: white;
            border-radius: 20px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0066cc;
        }
        h2 {
            padding: 10px;
            background-color: #fff;
            margin: 0;
            font-size: 18px;
            border-bottom: 1px solid #eee;
        }
        h3 {
            margin: 0 0 10px 0;
            font-size: 16px;
        }
    </style>
</head>
<body>
    <h2>Welcome, Admin ${sessionScope.account.userName}</h2>
    <div class="container">
        <div id="user-list">
            <h3>Online Users</h3>
            <select id="user-select" size="10"></select>
        </div>
        <div class="chat-area">
            <div id="chat-box"></div>
            <div class="input-area">
                <input type="text" id="message-input" placeholder="Type your message...">
                <button onclick="sendMessage()">Send</button>
            </div>
        </div>
    </div>

    <script>
        const adminId = "${sessionScope.account.id}";
        const adminName = "${sessionScope.account.userName}";
        const ws = new WebSocket("ws://" + window.location.host + "${pageContext.request.contextPath}/chat/admin_" + adminId);
        const chatBox = document.getElementById("chat-box");
        const messageInput = document.getElementById("message-input");
        const userSelect = document.getElementById("user-select");
        let selectedUserId = null;

        ws.onopen = function() {
            console.log("Connected to chat server");
            ws.send("username:" + adminName);
        };

        ws.onmessage = function(event) {
            const data = event.data;
            if (data.startsWith("online:")) {
                updateOnlineUsers(data.substring(7));
            } else {
                const p = document.createElement("p");
                p.textContent = data;
                chatBox.appendChild(p);
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        };

        function updateOnlineUsers(usersJson) {
            const users = JSON.parse(usersJson);
            userSelect.innerHTML = "";
            for (let userId in users) {
                if (!userId.startsWith("admin_")) {
                    const option = document.createElement("option");
                    option.value = userId;
                    option.text = users[userId];
                    userSelect.appendChild(option);
                }
            }
        }

        userSelect.onchange = function() {
            selectedUserId = userSelect.value;
            chatBox.innerHTML = "";
            if (selectedUserId) {
                ws.send("history:" + selectedUserId);
            }
        };

        function sendMessage() {
            if (!selectedUserId) {
                alert("Please select a user to chat with");
                return;
            }
            const content = messageInput.value.trim();
            if (content) {
                ws.send(selectedUserId + ":" + content);
                const p = document.createElement("p");
                p.textContent = "You: " + content;
                chatBox.appendChild(p);
                chatBox.scrollTop = chatBox.scrollHeight;
                messageInput.value = "";
            }
        }

        messageInput.addEventListener("keypress", function(event) {
            if (event.key === "Enter") {
                sendMessage();
            }
        });
    </script>
</body>
</html>