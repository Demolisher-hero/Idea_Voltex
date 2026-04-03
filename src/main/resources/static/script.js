document.querySelector("form").addEventListener("submit", function(e){

let user = document.querySelector("input[name='userId']").value;
let pass = document.querySelector("input[name='password']").value;

if(user.length < 3){
alert("User ID too short");
e.preventDefault();
}

if(pass.length < 6){
alert("Password must be at least 6 characters");
e.preventDefault();
}

});