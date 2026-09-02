async function getAllUsersNames() {
    try {
        let response = await fetch("http://localhost:8080/users");

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        let users = await response.json();
        users.forEach(user => {
            console.log(user.username);
        })


    } catch (error) {
        console.error("Failed to fetch users:", error);
    }
}
getAllUsersNames()


