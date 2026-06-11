package com.devdad.Forma.testutil;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.dto.user.LoginResponse;
import com.devdad.Forma.model.dto.user.UserLoginResponse;
import com.devdad.Forma.model.dto.user.UserRegisterResponse;
import com.devdad.Forma.model.dto.user.UserResponse;
import com.devdad.Forma.model.dto.user.UserUpdateResponse;

/**
 * UserTestData
 */
public class UserTestData {

	public static User user(){
		return User.builder()
			// .id(1)
			.email("test@forma.com")
			.firstName("Forma")
			.lastName("Tests")
			.password("helloWorld")
			.role("USER")
			.build();
	}

	public static UserRegisterResponse userRegisterResponse() {
		return new UserRegisterResponse("test@forma.com", "helloWorld", "Forma", "Tests");
	}

	public static UserLoginResponse userLoginResponse() {
		return new UserLoginResponse("test@forma.com", "helloWorld");
	}

	public static UserResponse userResponse() {
		return new UserResponse(1, "test@forma.com", "Forma", "Tests", "USER", null);
	}

	public static UserUpdateResponse userUpdateResponse() {
		return new UserUpdateResponse("Forma", "Tests", "test@forma.com");
	}

	public static LoginResponse loginResponse() {
		return new LoginResponse(user(), "test-token");
	}
}
