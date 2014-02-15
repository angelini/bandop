package com.mcgill.bandop.controllers;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.mcgill.bandop.BanditWorker;
import com.mcgill.bandop.Database;

public class ApplicationController {

	@Context
	private ServletContext context;

	@Context
	private HttpHeaders httpHeaders;

    @Context
    private ResourceContext resourceContext;

	public Database getDB() {
		return (Database) context.getAttribute("db");
	}

	public BanditWorker getWorker() {
		return (BanditWorker) context.getAttribute("worker");
	}

	public StandardPBEStringEncryptor getEncryptor() {
		return (StandardPBEStringEncryptor) context.getAttribute("encryptor");
	}

	public String encrypt(String unencryptedValue) {
		return getEncryptor().encrypt(unencryptedValue);
	}

	public String decrypt(String encryptedValue) {
		return getEncryptor().decrypt(encryptedValue);
	}

	public Map<String, Cookie> getCookies() {
		return httpHeaders.getCookies();
	}

    public ResourceContext getResourceContext() {
        return resourceContext;
    }

}
