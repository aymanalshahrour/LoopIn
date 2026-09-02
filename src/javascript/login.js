document.addEventListener("DOMContentLoaded", () => {
    
    const loginForm = document.querySelector("form");
    
    if (loginForm) {
        loginForm.addEventListener("submit", async (event) => {
            event.preventDefault();

            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;

            const credentials = {
                username: username,
                password: password
            };

            try {
                const response = await fetch("http://localhost:8080/users/", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(credentials)
                });

                if (!response.ok) {
                    const errorMessage = await response.text();
                    throw new Error(errorMessage || "Invalid username or password");
                }

                const data = await response.json();
                console.log("Login successful:", data);



            } catch (error) {
                console.error("Login error:", error);
                alert(error.message); 
            }
        });
    }
});