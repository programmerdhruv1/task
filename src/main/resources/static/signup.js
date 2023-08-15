/* start user registration process */

//Login
function signupPage(btn) {
    console.log('call login')
	$("#signup-message").text('');
	var form = $(btn).closest("form");
	var values = {};
	$.each($(form).serializeArray(), function(i, field) {
		values[field.name] = field.value;
	});

	if (values.username == null || values.username === '') {
		$("#signup-message").text('Please enter username.');
	} else if (values.password == null || values.password === '') {
		$("#signup-message").text('Please enter password.');
	} else {
		$.ajax({
			contentType: 'application/x-www-form-urlencoded',
			type: "POST",
			url: contextURL + "doLogin",
			data: values,
			success: function(data) {
				setTimeout(function() {
					window.location.href = 'http://localhost:8080';
				}, 500);
			},
			error: function(error) {
			    console.log(error)
				$("#signup-message").text('Invalid login credentials.');
			}
		})
	}
}

/*Register*/

function registerPage(btn) {
    console.log('call registerPage')
	$("#register-message").text('');
	var form = $(btn).closest("form");
	var values = {};
	$.each($(form).serializeArray(), function(i, field) {
		values[field.name] = field.value;
	});

	console.log(values);

	if (values.firstName == null || values.firstName === '') {
		$("#register-message").text('Please enter firstName.');
	} else if (values.lastName == null || values.lastName === '') {
		$("#register-message").text('Please enter lastName.');
	} else if (values.age == null || values.age === '') {
        $("#register-message").text('Please enter age.');
    } else if (values.gender == null || values.gender === '') {
        $("#register-message").text('Please enter gender.');
    } else if (values.address == null || values.address === '') {
        $("#register-message").text('Please enter address.');
    } else if (values.email == null || values.email === '') {
        $("#register-message").text('Please enter email.');
    } else if (values.number == null || values.number === '') {
        $("#register-message").text('Please enter number.');
    } else if (values.username == null || values.username === '') {
        $("#register-message").text('Please enter username.');
    } else if (values.password == null || values.password === '') {
        $("#register-message").text('Please enter password.');
    } else {
            $.ajax({
                contentType: 'application/x-www-form-urlencoded',
                type : "POST",
                url : contextURL + "register/checkEmail",
                data : {"email": values.email},
                success: function(data) {
                    $.ajax({
                        contentType: 'application/x-www-form-urlencoded',
                        type: "POST",
                        url: contextURL + "register",
                        data: values,
                        success: function(data) {
                            swal("Created!", "Profile is created!", "success");
                            setTimeout(function() {
                                window.location.href = 'http://localhost:8080';
                            }, 500);
                        },
                        error: function(error) {
                            $("#register-message").text('Username exiest.');
                        }
                    })
                },
                error: function(error) {
                    swal("Error!", error.responseText, "error");
                    $("#register-message").text('');
                }
            })
	}
}

/*Update*/

function updateProfile(btn) {
    console.log('call updateProfile')
	$("#register-message").text('');
	var form = $(btn).closest("form");
	var values = {};
	$.each($(form).serializeArray(), function(i, field) {
		values[field.name] = field.value;
	});

	console.log(values);

	if (values.firstName == null || values.firstName === '') {
		$("#register-message").text('Please enter firstName.');
	} else if (values.lastName == null || values.lastName === '') {
		$("#register-message").text('Please enter lastName.');
	} else if (values.age == null || values.age === '') {
        $("#register-message").text('Please enter age.');
    } else if (values.gender == null || values.gender === '') {
        $("#register-message").text('Please enter gender.');
    } else if (values.address == null || values.address === '') {
        $("#register-message").text('Please enter address.');
    } else if (values.email == null || values.email === '') {
        $("#register-message").text('Please enter email.');
    } else if (values.number == null || values.number === '') {
        $("#register-message").text('Please enter number.');
    } else if (values.username == null || values.username === '') {
        $("#register-message").text('Please enter username.');
    } else {
         $.ajax({
            contentType: 'application/x-www-form-urlencoded',
            type: "POST",
            url: contextURL + "profile/update",
            data: values,
            success: function(data) {
                swal("Updated!", "Profile details updated!", "success");
                setTimeout(function() {
                    window.location.href = 'http://localhost:8080/profile';
                }, 700);
            },
            error: function(error) {
                swal("Error!", error.responseText, "error");
                $("#register-message").text(error.responseText);
            }
        })
	}
}


//Forgot
function forgotEmail(btn) {
    console.log('call forgotEmail')
	$("#forgot-message").text('');
	var form = $(btn).closest("form");
	var values = {};
	$.each($(form).serializeArray(), function(i, field) {
		values[field.name] = field.value;
	});

	if (values.email == null || values.email === '') {
		$("#forgot-message").text('Please enter email.');
	} else {
		$.ajax({
			contentType: 'application/x-www-form-urlencoded',
			type: "POST",
			url : contextURL + "register/verifyEmail",
            data : {"email": values.email},
			success: function(data) {
			    console.log(data);
			    $("#step-1").hide();
                $("#id").val(data);
                $("#password").val('');
                $("#password-new").val('');
                $("#step-2").show();
			},
			error: function(error) {
				swal("Error!", error.responseText, "error");
			}
		})
	}
}

function changePassword(btn) {
    console.log('call forgotEmail')
	var form = $(btn).closest("form");
	var values = {};
	$.each($(form).serializeArray(), function(i, field) {
		values[field.name] = field.value;
	});
    console.log(values);
    $.ajax({
        contentType: 'application/x-www-form-urlencoded',
        type: "PUT",
        url : contextURL + "forgot/changePassword",
        data : {"id": values.id, "password": values.passwordOne, "passwordTwo": values.passwordTwo},
        success: function(data) {
           swal("Success!", "Password has been changed.", "success");
           setTimeout(function() {
               window.location.href = 'http://localhost:8080/login';
           }, 500);
        },
        error: function(error) {
            swal("Error!", error.responseText, "error");
        }
    })
}

function initChangePassword(data){
    $("#step-1").hide();
    $("#id").value(data);
    $("#passwordOne").value('');
    $("#passwordTwo").value('');
    $("#step-2").show();
}