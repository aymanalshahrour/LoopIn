async function loginfunction(event){
    event.preventDefault();

    const usernameOrEmail = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;
    const errorMessage = document.getElementById("login-error");

    if (errorMessage) {
        errorMessage.textContent = "";
    }

    try {
        const loginUsername = await getLoginUsername(usernameOrEmail);

        const response = await fetch(
            `http://localhost:8080/users/user/${(loginUsername)}/${(password)}`
        );

        if (!response.ok) {
            throw new Error(`Login request failed with status ${response.status}`);
        }

        const isValidUser = await response.json();

        if (isValidUser === true) {
            window.location.assign("http://localhost:63343/LoopIn/src/html/main.html?_ijt=ok3c1i464eih2e7d08861blatj");
            return;
        }

        if (errorMessage) {
            errorMessage.textContent = "Username/email or password is incorrect.";
        }
    } catch (error) {
        console.error("Login failed:", error);
        if (errorMessage) {
            errorMessage.textContent = "Cannot connect to the server. Check that the backend is running.";
        }
    }
}

async function getLoginUsername(usernameOrEmail) {
    if (!usernameOrEmail.includes("@")) {
        return usernameOrEmail;
    }

    const response = await fetch("http://localhost:8080/users");

    if (!response.ok) {
        throw new Error(`Users request failed with status ${response.status}`);
    }

    const users = await response.json();
    const matchingUser = users.find((user) => user.email === usernameOrEmail);

    return matchingUser ? matchingUser.username : usernameOrEmail;
}

document.addEventListener("DOMContentLoaded", () => {
    document
        .getElementById("login-form")
        .addEventListener("submit", loginfunction);
});
