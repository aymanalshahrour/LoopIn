let realtimeSocket;

function getCookie(name) {
    const cookies = document.cookie.split("; ");
    const cookie = cookies.find((item) => item.startsWith(`${name}=`));

    if (!cookie) {
        return "";
    }

    return decodeURIComponent(cookie.substring(name.length + 1));
}

async function loadContacts() {
    const currentUser = localStorage.getItem('username');
    const sessionToken = getCookie("sessionToken");

    const response = await fetch("http://localhost:8080/friendship/showonlyuserfriends", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${sessionToken}`
        }
    });

    const contacts = await response.json();

    const contactsList = document.getElementById("contact-list");

    contactsList.innerHTML = "";

    contacts.forEach(username => {

        // Don't show yourself
        if (username === currentUser) {
            return;
        }

        contactsList.innerHTML += `
            <div class="contact" onclick="openChat('${username}')">
                <div class="contact-avatar"
                     style="background-color: blue;">
                </div>

                <div class="contact-info">
                    <div class="contact-name">${username}</div>
                    <div class="contact-last-msg">
                        Click to chat
                    </div>
                </div>
            </div>
        `;
    });
}

async function sendMessage() {
    const messageInput = document.getElementById("message-input");
    const messageValue = messageInput.value.trim();
    const sendToUser = document.getElementById("active-chat-name").textContent.trim();
    const sender = localStorage.getItem('username');
    const sessionToken = getCookie("sessionToken");

    // Prevent sending blank messages or sending without selecting a chat
    if (!messageValue || sendToUser === "Select a chat") {
        return;
    }

    try {
        const message = {
            senderUsername: sender,
            receiverUsername: sendToUser,
            content: messageValue,
        };

        const sendResponse = await fetch("http://localhost:8080/messages/send", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${sessionToken}`
            },
            body: JSON.stringify(message)
        });

        if (!sendResponse.ok) {
            const errorMessage = await sendResponse.text();
            throw new Error(errorMessage || "Failed to send message");
        }

        const savedMessage = await sendResponse.json();

        // Render the bubble into the chat window
        displayMessage(savedMessage);

        // Clear the input field
        messageInput.value = "";

    } catch (error) {
        console.error("Error:", error);
    }
}

async function openChat(username) {
    console.log("Opening chat with:", username);
    const currentUser = localStorage.getItem('username');
    const sessionToken = getCookie("sessionToken");

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
                "Content-Type": "application/json",
                "Authorization": `Bearer ${sessionToken}`
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

function displayMessage(message) {
    const messagesContainer = document.getElementById("chat-messages");
    const currentUser = localStorage.getItem("username");

    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message");

    if (message.senderUsername === currentUser) {
        messageDiv.classList.add("sent");
    } else {
        messageDiv.classList.add("received");
    }

    messageDiv.textContent = message.content;
    messagesContainer.appendChild(messageDiv);

    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function connectRealtime() {
    if (realtimeSocket && realtimeSocket.readyState !== WebSocket.CLOSED) {
        return;
    }

    realtimeSocket = new WebSocket("ws://localhost:8080/realtime");

    realtimeSocket.onmessage = async function(event) {
        const realtimeMessage = JSON.parse(event.data);
        const currentUser = localStorage.getItem("username");
        const activeChat = localStorage.getItem("activeChat");

        if (realtimeMessage.type === "message_sent") {
            const message = realtimeMessage.data;
            const isMyChat =
                (message.senderUsername === activeChat && message.receiverUsername === currentUser) ||
                (message.senderUsername === currentUser && message.receiverUsername === activeChat);

            if (isMyChat && message.senderUsername !== currentUser) {
                displayMessage(message);
            }
        }

        if (realtimeMessage.type === "friend_request_sent") {
            const friendship = realtimeMessage.data;

            if (friendship.receiverUsername === currentUser) {
                await loadFriendRequests();
            }
        }

        if (realtimeMessage.type === "friend_request_accepted" || realtimeMessage.type === "friend_request_declined") {
            const friendship = realtimeMessage.data;

            if (friendship.senderUsername === currentUser || friendship.receiverUsername === currentUser) {
                await loadContacts();
                await loadFriendRequests();
            }
        }
    };

    realtimeSocket.onclose = function() {
        setTimeout(connectRealtime, 1000);
    };
}

async function acceptFriend(friendUsername){
    const sessionToken = getCookie("sessionToken");

    try {
        const response = await fetch("http://localhost:8080/friendship/changefriendstatus", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${sessionToken}`
            },
            body: JSON.stringify(friendUsername)
        });

        if (!response.ok) {
            throw new Error(`Accept request failed with status ${response.status}`);
        }

        await openAddFriendBox();
        await loadContacts();
    } catch (error) {
        console.error("Error accepting friend request:", error);
    }
}

async function openAddFriendBox() {
    document.getElementById('add-friend-box').style.display = 'flex';
    await loadFriendRequests();
}

async function loadFriendRequests() {
    const currentUser = localStorage.getItem('username');
    const sessionToken = getCookie("sessionToken");
    console.log("Current user:", currentUser);

    try {
        const response = await fetch("http://localhost:8080/friendship/showfriendrequests", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${sessionToken}`
            }
        });

        let friendsList = await response.json();

        if (!Array.isArray(friendsList) || friendsList.length === 0) {
            const allFriendshipsResponse = await fetch("http://localhost:8080/friendship");
            const allFriendships = await allFriendshipsResponse.json();

            friendsList = [];

            for (const friendship of allFriendships) {
                const receiverUsername = friendship.receiverUsername;
                const status = friendship.status;

                if (
                    receiverUsername &&
                    receiverUsername.toLowerCase() === currentUser.toLowerCase() &&
                    status &&
                    status.toLowerCase() === "pending"
                ) {
                    friendsList.push(friendship.senderUsername);
                }
            }
        }

        console.log(friendsList);

        renderFriendRequests(friendsList);

    } catch (error) {
        console.error("Error fetching friends:", error);
    }
}

function renderFriendRequests(friendsList) {
    const container = document.getElementById('requests-list-container');

    container.innerHTML = '';

    if (!Array.isArray(friendsList) || friendsList.length === 0) {
        container.innerHTML = '<div class="empty-requests">No friend requests</div>';
        return;
    }

    friendsList.forEach(friendUsername => {
        const initial = friendUsername.charAt(0).toUpperCase();

        const itemHtml = `
                <div class="request-item">
                    <div class="request-info">
                        <div class="req-avatar">${initial}</div>
                        <div class="req-name">${friendUsername}</div>
                    </div>
                    <div class="request-actions">
                        <button class="accept-btn" title="Accept" onclick="acceptFriend('${friendUsername}')">✓</button>
                        <button class="decline-btn" title="Decline" onclick="declineFriend('${friendUsername}')">✗</button>
                    </div>
                </div>
            `;

        container.insertAdjacentHTML('beforeend', itemHtml);
    });
}

function closeAddFriendBox() {
    document.getElementById('add-friend-box').style.display = 'none';
}

async function addFriend() {
    const inputField = document.getElementById('friend-username');
    const username = inputField.value.trim();
    const sessionToken = getCookie("sessionToken");

    if (username !== "") {
        try {
            const response = await fetch("http://localhost:8080/friendship/send", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${sessionToken}`
                },
                body: JSON.stringify({
                    receiverUsername: username
                })
            });

            if (!response.ok) {
                throw new Error(`Add friend failed with status ${response.status}`);
            }

            alert("Friend request sent to " + username + "!");
            inputField.value = "";
            await loadFriendRequests();
        } catch (error) {
            console.error("Error sending friend request:", error);
            alert("Could not send friend request.");
        }
    } else {
        alert("Please enter a username first.");
    }
}

async function declineFriend(friendUsername) {
    const sessionToken = getCookie("sessionToken");

    try {
        const response = await fetch("http://localhost:8080/friendship/decline", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${sessionToken}`
            },
            body: JSON.stringify(friendUsername)
        });

        if (!response.ok) {
            throw new Error(`Decline request failed with status ${response.status}`);
        }

        await loadFriendRequests();
    } catch (error) {
        console.error("Error declining friend request:", error);
    }
}

window.onclick = function(event) {
    const modal = document.getElementById('add-friend-box');
    if (event.target === modal) {
        closeAddFriendBox();
    }
}

document.addEventListener("DOMContentLoaded", function() {
    loadContacts();
    loadFriendRequests();
    connectRealtime();
});
