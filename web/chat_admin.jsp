<%-- 
    Document   : chat_admin
    Created on : 24 thg 3, 2025, 00:55:21
    Author     : tranhongphuoc
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Chat</title>
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
            position: fixed;
            bottom: 0;
            width: 100%;
            max-width: 1200px;
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
    </style>
</head>
<body>
    <h2>Welcome, ${sessionScope.account.userName}</h2>
    <div class="container">
        <div id="chat-box"></div>
    </div>
    <div class="input-area">
        <input type="text" id="message-input" placeholder="Type your message to admins...">
        <button onclick="sendMessage()">Send</button>
    </div>

    <script>
        const userId = "${sessionScope.account.id}";
        const username = "${sessionScope.account.userName}";
        const ws = new WebSocket("ws://" + window.location.host + "${pageContext.request.contextPath}/chat/" + userId);
        const chatBox = document.getElementById("chat-box");
        const messageInput = document.getElementById("message-input");

        ws.onopen = function() {
            console.log("Connected to chat server");
            ws.send("username:" + username);
        };

        ws.onmessage = function(event) {
            const message = event.data;
            const p = document.createElement("p");
            p.textContent = message;
            chatBox.appendChild(p);
            chatBox.scrollTop = chatBox.scrollHeight;
        };

        ws.onclose = function() {
            console.log("Disconnected from chat server");
        };

        function sendMessage() {
            const content = messageInput.value.trim();
            if (content) {
                ws.send("all_admins:" + content);
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