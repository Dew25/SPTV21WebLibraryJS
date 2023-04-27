import {userModule} from './UserModule.js';
import {loginModule} from './LoginModule.js';

export {checkAuthorization};
let newUser = document.getElementById('newUser');
newUser.addEventListener('click',e=>{
    e.preventDefault();
    userModule.printNewUserForm();
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
    if(sessionStorage.getItem("authUser")!== null){
        console.log(JSON.parse(sessionStorage.getItem("authUser")));
    }else{
        console.log("Авторизация отсутствует");
    }
}
checkAuthorization();
