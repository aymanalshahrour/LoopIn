async function loadContacts() {
    const currentUser = localStorage.getItem('username');

    const response = await fetch('http://localhost:8080/users');
    const contacts = await response.json();

    const contactsList = document.getElementById("contact-list");

    contactsList.innerHTML = "";

    contacts.forEach(user => {

        // Don't show yourself
        if (user.username === currentUser) {
            return;
        }

        contactsList.innerHTML += `
            <div class="contact" onclick="openChat('${user.username}')">
                <div class="contact-avatar"
                     style="background-color: blue;">
                </div>

                <div class="contact-info">
                    <div class="contact-name">${user.username}</div>
                    <div class="contact-last-msg">
                        Click to chat
                    </div>
                </div>
            </div>
        `;
    });
}

function openChat(username) {
    console.log("Opening chat with:", username);

    // Save who we are chatting with
    localStorage.setItem("activeChat", username);

    // Change chat header
    document.querySelector(".chat-header .contact-name").textContent = username;

    // Clear old messages
    document.getElementById("chat-messages").innerHTML = "";

    // TODO: Load messages between currentUser and username
}

async function sendMessage() {
    const messageInput = document.getElementById("message-input");
    const messageValue = messageInput.value.trim();
    const sendToUser = document.getElementById("active-chat-name").textContent.trim();
    const sender = localStorage.getItem('username');

    // Prevent sending blank messages or sending without selecting a chat
    if (!messageValue || sendToUser === "Select a chat") {
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/users");
        const users = await response.json();
        const validSender = users.find((user) => user.username === sender);
        const validReceiver = users.find((user) => user.username === sendToUser);

        if (!validSender || !validReceiver) {
            console.error("Invalid Sender/Receiver !");
            return;
        }

        const message = {
            senderUsername: sender,
            receiverUsername: sendToUser,
            content: messageValue,
        };

        const sendResponse = await fetch("http://localhost:8080/messages/send", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(message)
        });

        if (!sendResponse.ok) {
            const errorMessage = await sendResponse.text();
            throw new Error(errorMessage || "Failed to send message");
        }

        // Render the bubble into the chat window
        displayMessage(message);

        // Clear the input field
        messageInput.value = "";

    } catch (error) {
        console.error("Error:", error);
    }
}

loadContacts();
async function openChat(username) {
    console.log("Opening chat with:", username);
    const currentUser = localStorage.getItem('username');

    // Save who we are chatting with
    localStorage.setItem("activeChat", username);

    // Change chat header
    document.querySelector(".chat-header .contact-name").textContent = username;

    // Clear old messages
    const chatMessages = document.getElementById("chat-messages");
    chatMessages.innerHTML = "";

    // Fetch and load messages between currentUser and username
    try {
        const response = await fetch("http://localhost:8080/messages/messageuserandreciver", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                user1: currentUser,
                user2: username
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const messages = await response.json();

        // Loop through the history and render each message
        messages.forEach(message => {
            displayMessage(message);
        });

    } catch (error) {
        console.error("Error fetching chat history:", error);
    }
}

async function displayMessage(message) {
    const messagesContainer = document.getElementById("chat-messages");
    const currentUser = localStorage.getItem("username");

    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message");

    // Right for sender, left for receiver
    if (message.senderUsername === currentUser) {
        messageDiv.classList.add("sent");
    } else {
        messageDiv.classList.add("received");
    }

    messageDiv.textContent = message.content;
    messagesContainer.appendChild(messageDiv);

    // Auto-scroll to the newest message
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

