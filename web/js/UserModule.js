class UserModule{
    printNewUserForm(){
        let html = 
        `<h3 class="w-100 d-flex justify-content-center mt-5">Регистрация читателя</h3>
          <div class="w-100 p-3 d-flex justify-content-center">
            <form action="createReader" method="POST">
                <div class="card border-0 m-2" style="width: 30rem;">
                    <div class="mb-3 row">
                        <label for="inputName" class="col-sm-3 col-form-label">Имя</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputFirstname" name="firstname">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputLastname" class="col-sm-3 col-form-label">Фамилия</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputLastname" name="lastname">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputPhone" class="col-sm-3 col-form-label">Телефон</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputPhone" name="phone">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputLogin" class="col-sm-3 col-form-label">Логин</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputLogin" name="login">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputPassword" class="col-sm-3 col-form-label">Пароль</label>
                        <div class="col-sm-9">
                          <input type="password" class="form-control" id="inputPassword" name="password">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <div class="col-sm-12 d-flex justify-content-end">
                            <button id="btnNewUser" class="btn btn-primary col-sm-9" type="submit">Зарегистрировать</button>
                        </div>
                    </div>
                </div>
            </form>
          </div>
        </div>`;
        document.getElementById('content').innerHTML = html;
        let btnNewUser = document.getElementById('btnNewUser');
        btnNewUser.addEventListener('click',e=>{
            e.preventDefault();
            userModule.sentNewUserData();
        });
    }
    sentNewUserData(){
        const newUserData = {
            "firstname": document.getElementById("inputFirstname").value,
            "lastname": document.getElementById('inputLastname').value,
            "phone": document.getElementById('inputPhone').value,
            "login": document.getElementById('inputLogin').value,
            "password": document.getElementById('inputPassword').value
        }
        fetch('createUser',{
            method: "POST",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(newUserData)
        })
                .then(response => response.json())
                .then(response =>{
                    if(response.status){
                        document.getElementById('info').innerHTML=response.info;
                        document.getElementById('content').innerHTML = '';
                    }else{
                        document.getElementById('info').innerHTML=response.info;
                    }
                })
                .catch(error => document.getElementById('info').innerHTML='Пользователь не сохранен ('+e+')');


    }
};
const userModule = new UserModule();
export {userModule};

