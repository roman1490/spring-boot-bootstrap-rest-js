async function fillUserInformationTable() {

    let response = await fetch('/api/user');
    let myJson = await response.json();
    $('#userId').text(myJson.id)
    $('#userFirstName').text(myJson.firstName)
    $('#userLastName').text(myJson.lastName)
    $('#userAge').text(myJson.age)
    $('#userEmail').text(myJson.email)
    $('#userRoles').text(getStringRolesList(myJson.roles))

    $('#header-link').prepend(`<strong>${myJson.email}</strong> with roles: ${getStringRolesList(myJson.roles)}`)

}

fillUserInformationTable();

function getStringRolesList(rolesArr){
    let userRolesList = "";
    for (let i = 0; i < rolesArr.length; i++) {
        if (rolesArr[i].role === "ROLE_USER") {
            userRolesList += " USER ";
        } else if (rolesArr[i].role === "ROLE_ADMIN") {
            userRolesList += " ADMIN ";
        }
    }
    return userRolesList;
}

function fillUserListTable() {
    fetch('/api/admin')
        .then(response => response.json())
        .then(data => {
            data.forEach(function(u) {
                $('#userList-tbody').append(`
                    <tr>
                        <td scope="col">${u.id}</td>
                        <td scope="col">${u.firstName}</td>
                        <td scope="col">${u.lastName}</td>
                        <td scope="col">${u.age}</td>
                        <td scope="col">${u.email}</td>
                        <td scope="col">${getStringRolesList(u.roles)}</td>
                        <td scope="col">
                            <button type="button" class="btn-primary btn-sm" data-toggle="modal"
                                th:data-target="#editUser" onclick="showModalWindowAndFillForm(${u.id}, 'edit')">
                                Edit
                            </button>
                        </td>
                        <td scope="col">
                            <button type="button" class="btn btn-danger btn-sm" data-toggle="modal"
                                th:data-target="#deleteUser" onclick="showModalWindowAndFillForm(${u.id}, 'delete')">
                                Delete
                            </button>
                        </td>
                    </tr>
                `)
            })
        })
}

function showModalWindowAndFillForm(id, action) {
    $('#' + action + 'User').modal('show');
    fetch('/api/user/' + id)
        .then(response => response.json())
        .then(data => {
            // console.log(data)
            $('#id-' + action).attr('value', data.id);
            $('#firstName-' + action).attr('value', data.firstName);
            $('#lastName-' + action).attr('value', data.lastName);
            $('#age-' + action).attr('value', data.age);
            $('#email-' + action).attr('value', data.email);

            stringUserRoles = getStringRolesList(data.roles);
            $('option').removeAttr('selected')
            if (stringUserRoles.includes("USER")) {
                $('option.ROLE_USER').attr('selected', 'selected');
            }
            if (stringUserRoles.includes("ADMIN")) {
                $('option.ROLE_ADMIN').attr('selected', 'selected');
            }
        });
}

fillUserListTable();

newUserForm.onsubmit = async (e) => {
    e.preventDefault();

    fetch('/api/admin', {
        method: 'POST',
        body: new FormData(newUserForm)
    });
    location.reload();
};

editUserForm.onsubmit = async (e) => {
    e.preventDefault();

    fetch('/api/admin/updateUser/' + $('#id-edit').val(), {
        method: 'PATCH',
        body: new FormData(editUserForm)
    });
    location.reload();
};

deleteUserForm.onsubmit = async (e) => {
    e.preventDefault();

    fetch('/api/admin/delete/' + $('#id-delete').val(), {
        method: 'DELETE'
    })
    location.reload();
};