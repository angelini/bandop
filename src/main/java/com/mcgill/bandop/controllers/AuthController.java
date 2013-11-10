package com.mcgill.bandop.controllers;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.mcgill.bandop.exceptions.BadRequestException;
import com.mcgill.bandop.exceptions.UnauthorizedException;
import com.mcgill.bandop.models.User;

@Path("/auth")
public class AuthController extends ApplicationController {

	static final String LOGIN_COOKIE = "_bandop_login";

	public static int getLoggedInUser(Map<String, Cookie> cookies, StandardPBEStringEncryptor encryptor) {
		Cookie cookie = cookies.get(LOGIN_COOKIE);

		if (cookie == null) {
			throw new UnauthorizedException("No login information provided");
		}

		try {
			String idString = encryptor.decrypt(cookie.getValue());

			return Integer.parseInt(idString);

		} catch (NumberFormatException e) {
			throw new UnauthorizedException("Invalid login");
		}
	}

	@GET @Path("current")
	@Produces(MediaType.APPLICATION_JSON)
	public User currentUser() {
		int userId = AuthController.getLoggedInUser(getCookies(), getEncryptor());
		return User.loadUser(getDB(), userId);
	}

	@POST @Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		if (user.getEmail() == null || user.getPassword() == null) {
			throw new BadRequestException("ID and Password required");
		}

		User savedUser = User.loadUser(getDB(), user.getEmail());

		if (!encrypt(user.getPassword()).equals(savedUser.getPassword())) {
			throw new UnauthorizedException("Password does not match");
		}

		savedUser.setKey(encrypt(Integer.toString(savedUser.getId())));

		return Response.status(Response.Status.OK)
				.entity(savedUser).type(MediaType.APPLICATION_JSON)
				.cookie(new NewCookie(LOGIN_COOKIE, encrypt(Integer.toString(savedUser.getId())), "/", "", null, 0, false, false))
				.build();
	}

}
