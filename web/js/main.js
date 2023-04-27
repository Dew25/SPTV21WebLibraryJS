import {userModule} from './UserModule.js';
import {loginModule} from './LoginModule.js';

export {checkAuthorization};

let newUser = document.getElementById('newUser');
newUser.addEventListener('click',e=>{
    e.preventDefault();
    userModule.printNewUserForm();
});
let userProfile = document.getElementById('userProfile');
userProfile.addEventListener('click',e=>{
    e.preventDefault();
    userModule.printProfileForm();
});
let logIn = document.getElementById('logIn');
logIn.addEventListener('click',e=>{
    e.preventDefault();
    loginModule.printFormLogIn();
});
let logout = document.getElementById('logout');
logout.addEventListener('click',e=>{
    e.preventDefault();
    loginModule.sendLogout();
});


function checkAuthorization(){
    
    let authUser = sessionStorage.getItem("authUser");
    if(authUser === null){
        console.log("Авторизация отсутствует");
        document.getElementById('userLogin').hidden = true;
        logIn.hidden = false;
        logout.hidden = true;
        userProfile.hidden = true;
        return;
    }
    authUser = JSON.parse(authUser);
    console.log(authUser);
    document.getElementById('userLogin').hidden = false;
    document.getElementById('userLogin').innerHTML = authUser.login;
    logIn.hidden = true; //скрыть
    logout.hidden = false; //показать
    let USER = false;
    let MANAGER = false;
    let ADMINISTRATOR = false;
    for(let index in authUser.roles){
        if("USER" === authUser[index]) USER = true;
        if("MANAGER" === authUser[index]) MANAGER = true;
        if("ADMINISTRATOR" === authUser[index]) ADMINISTRATOR = true;
    }
    
    if(USER){
        newUser.hidden=false;
        userProfile.hidden=false;
    }
    
}
checkAuthorization();
