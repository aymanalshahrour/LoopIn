async function loadContacts() {
    const currentUser = localStorage.getItem('username');

    const response = await fetch('http://localhost:8080/users');
    const contacts = await response.json();

    let friends = [];

    contacts.forEach(contact => {
        if (contact.username !== currentUser) {
            friends.unshift(contact);
        }
    });

    const contactsList = document.getElementById("contact-list");

    contactsList.innerHTML = "";

    friends.forEach(user => {

        contactsList.innerHTML += `
            <div class="contact">
                <div class="contact-avatar"
                     style="background-color: #b300ff;">
                </div>

                <div class="contact-info">
                    <div class="contact-name">${user.username}</div>
                    <div class="contact-last-msg">${user.username}</div>
                </div>
            </div>
        `;


    });
}

loadContacts();