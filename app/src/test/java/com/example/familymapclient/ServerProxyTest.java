package com.example.familymapclient;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ServerProxyTest {

    private static final String TEST_USER = "test";

    ServerProxy serverProxy = new ServerProxy(null);

    @Test
    public void loginSuccess() {
        ServerProxy.HttpResponse response = serverProxy.invokeLogin("localhost", "8000", TEST_USER, "test");

        assertEquals(200, response.responseCode);
        assertTrue(response.response.contains("\"userName\":\"" + TEST_USER + "\""));
    }

    @Test
    public void loginFailureInvalidUsername() {
        ServerProxy.HttpResponse response = serverProxy.invokeLogin("localhost", "8000", "dummy", "test");

        assertEquals(500, response.responseCode);
    }

    @Test
    public void registerSuccess() {
        String username = UUID.randomUUID().toString();
        ServerProxy.HttpResponse response = serverProxy.invokeRegister("localhost", "8000", username, "test", "first", "last", "email", "m");

        assertEquals(200, response.responseCode);
        assertTrue(response.response.contains("\"userName\":\"" +  username + "\""));
    }

    @Test
    public void registerFailureEmptyUser() {
        ServerProxy.HttpResponse response = serverProxy.invokeRegister("localhost", "8000", "", "test", "first", "last", "email", "m");

        assertEquals(500, response.responseCode);
    }

    @Test
    public void registerFailureInvalidGender() {
        ServerProxy.HttpResponse response = serverProxy.invokeRegister("localhost", "8000", "", "test", "first", "last", "email", "a");

        assertEquals(500, response.responseCode);
    }
}