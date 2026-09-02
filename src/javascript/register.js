const validatePassword = (passworduser, passworduserconf) => {
    if (passworduser.length < 8) {
        console.log("Password length should be at least 8 characters");
        return false;
    }

    if (passworduser !== passworduserconf) {
        console.log("Passwords do not match");
        return false;
    }

    return true;
};

const validateEmail = (email) => {
    return String(email)
        .toLowerCase()
        .match(
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
};

async function registerUserToTheBackend(username, email, password) {

    const user = {
        username: username,
        email: email,
        password: password
    };

    try {
        const response = await fetch("http://localhost:8080/users/adduser", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage || "Registration failed");
        }

        const data = await response.json();
        console.log("User registered:", data);
        return data;

    } catch (error) {
        console.error("Error:", error);
    }
}

async function registerfunction(event) {
    event.preventDefault();
    
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const passwordConfirm = document.getElementById('confirm-password').value;

    if (!validateEmail(email)) {
        console.log("Invalid email");
        return false;
    }

    if (!validatePassword(password, passwordConfirm)) {
        return false;
    }

    await registerUserToTheBackend(username, email, password);

    console.log("Registration attempt finished");
}

document.addEventListener("DOMContentLoaded", () => {
    document
        .getElementById("register-form")
        .addEventListener("submit", registerfunction);
});
