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
    let sendMessageButton = document.getElementById("send-button");
    let messageValue = document.getElementById("message-input").value;
    let sendToUser = document.getElementById("active-chat-name").textContent;
    const sender = localStorage.getItem('username');


    console.log(sendMessageButton);
    console.log(messageValue);
    console.log(sendToUser);
    console.log(sender);

    let message = {
        senderUsername: sender,
        receiverUsername: sendToUser,
        content: messageValue,
    }

    try {
        const response = await fetch("http://localhost:8080/messages/send", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(message)
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage || "failed to send message");
        }


    } catch (error) {
        console.error("Error:", error);
    }
    document.getElementById("message-input").value = "";

}
loadContacts();