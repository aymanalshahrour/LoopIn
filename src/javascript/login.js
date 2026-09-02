
async function getAllUsers(){
    const response = await fetch("http://localhost:8080/users");
    const user = await response.json(); 
    user.forEach(u => {
    console.log(u.username);});
    
}
async function loginfunction(){


    const username = document.getElementById("username").value
    console.log(username)


}

getAllUsers()