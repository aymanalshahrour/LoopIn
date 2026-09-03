const currentUser = localStorage.getItem('currentUser')
console.log(currentUser)

async function loadContacts(){
    console.log(currentUser)
    const response = await fetch('http://localhost:8080/users')
    const contacts = await response.json()
    let friends = []

    contacts.forEach(contact => {
        if (contact.username !== currentUser){
            friends.unshift(contact)

        }
    })
    console.log(friends)

}


loadContacts()