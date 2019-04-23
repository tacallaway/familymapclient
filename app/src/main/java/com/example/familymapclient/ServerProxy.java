package com.example.familymapclient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ServerProxy {

    private Context context;

    public ServerProxy(Context context) {
        this.context = context;
    }

    public void login(String serverHost, String serverPort, String username, String password) {
        new SigninOperation(serverHost, serverPort, username, password).execute();
    }

    public void register(String serverHost, String serverPort, String username, String password, String firstName, String lastName, String email, String gender) {
        new RegisterOperation(serverHost, serverPort, username, password, firstName, lastName, email, gender).execute();
    }

    private class SigninOperation extends AsyncTask<Void, Void, ServiceCallResult> {

        private String serverHost;
        private String serverPort;
        private String username;
        private String password;

        SigninOperation(String serverHost, String serverPort, String username, String password) {
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.username = username;
            this.password = password;
        }

        @Override
        protected ServiceCallResult doInBackground(Void... params) {

            HttpResponse httpResponse = invokeLogin(serverHost, serverPort, username, password);

            return new ServiceCallResult(httpResponse.responseCode, httpResponse.response);
        }

        protected void onPostExecute(ServiceCallResult result) {
            if (result.responseCode < 200 || result.responseCode >= 300) {
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "Login Failed!", duration);
                toast.show();
            } else {
                new PersonOperation(serverHost, serverPort).execute(result);
            }
        }
    }

    protected HttpResponse invokeLogin(String serverHost, String serverPort, String username, String password) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("POST");

            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes("{\n" +
                    "\"userName\": \"" + username +"\",\n" +
                    "\"password\": \"" + password + "\"\n" +
                    "}");
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new HttpResponse(responseCode, response.toString());

        } catch (MalformedURLException mue) {
            return new HttpResponse(500, mue.getMessage());
        } catch (IOException ioe) {
            return new HttpResponse(500, ioe.getMessage());
        }
    }

    private class RegisterOperation extends AsyncTask<Void, Void, ServiceCallResult> {

        private String serverHost;
        private String serverPort;
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private String gender;

        RegisterOperation(String serverHost, String serverPort, String username, String password, String firstName, String lastName, String email, String gender) {
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.gender = gender;
        }

        @Override
        protected ServiceCallResult doInBackground(Void... params) {

            HttpResponse httpResponse = invokeRegister(serverHost, serverPort, username, password, firstName, lastName, email, gender);

            return new ServiceCallResult(httpResponse.responseCode, httpResponse.response);
        }

        protected void onPostExecute(ServiceCallResult result) {
            if (result.responseCode < 200 || result.responseCode >= 300) {
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "Register Failed!", duration);
                toast.show();
            } else {
                new PersonOperation(serverHost, serverPort).execute(result);
            }
        }
    }

    protected HttpResponse invokeRegister(String serverHost, String serverPort, String username, String password, String firstName, String lastName, String email, String gender) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            con.setRequestMethod("POST");

            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes("{\n" +
                    "\"userName\": \"" + username +"\",\n" +
                    "\"password\": \"" + password + "\",\n" +
                    "\"email\": \"" + email + "\",\n" +
                    "\"firstName\": \"" + firstName + "\",\n" +
                    "\"lastName\": \"" + lastName + "\",\n" +
                    "\"gender\": \"" + gender + "\"\n" +
                    "}");
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new HttpResponse(responseCode, response.toString());
        } catch (MalformedURLException mue) {
            return new HttpResponse(500, mue.getMessage());
        } catch (IOException ioe) {
            return new HttpResponse(500, ioe.getMessage());
        }
    }

    private class PersonOperation extends AsyncTask<ServiceCallResult, Void, ServiceCallResult> {

        private String serverHost;
        private String serverPort;
        private String authToken;
        private String personId;

        PersonOperation(String serverHost, String serverPort) {
            this.serverHost = serverHost;
            this.serverPort = serverPort;
        }

        @Override
        protected ServiceCallResult doInBackground(ServiceCallResult... params) {

            ServiceCallResult result = params[0];

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("GET");

                authToken = result.getJson().getString("authToken");
                personId = result.getJson().getString("personID");
                con.setRequestProperty("authorization", authToken);

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new ServiceCallResult(responseCode, response.toString());
            } catch (MalformedURLException mue) {
                return new ServiceCallResult(500, mue.getMessage());
            } catch (IOException ioe) {
                return new ServiceCallResult(500, ioe.getMessage());
            } catch (JSONException je) {
                return new ServiceCallResult(500, je.getMessage());
            }
        }

        protected void onPostExecute(ServiceCallResult result) {
            int duration = Toast.LENGTH_SHORT;

            if (result.responseCode < 200 || result.responseCode >= 300) {
                Toast toast = Toast.makeText(context, "Person Call Failed!", duration);
                toast.show();
            } else {
                try {
                    JSONArray list = result.getJson().getJSONArray("data");
                    new EventOperation(serverHost, serverPort, authToken, personId, list).execute(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class EventOperation extends AsyncTask<ServiceCallResult, Void, ServiceCallResult> {

        private JSONArray personsArray;
        private String serverHost;
        private String serverPort;
        private String authToken;
        private String personId;

        EventOperation(String serverHost, String serverPort, String authToken, String personId, JSONArray personsArray) {
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.personsArray = personsArray;
            this.authToken = authToken;
            this.personId = personId;
        }

        @Override
        protected ServiceCallResult doInBackground(ServiceCallResult... params) {

            ServiceCallResult result = params[0];

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("GET");

                con.setRequestProperty("authorization", authToken);

                int responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new ServiceCallResult(responseCode, response.toString());
            } catch (MalformedURLException mue) {
                return new ServiceCallResult(500, mue.getMessage());
            } catch (IOException ioe) {
                return new ServiceCallResult(500, ioe.getMessage());
            }
        }

        protected void onPostExecute(ServiceCallResult result) {
            int duration = Toast.LENGTH_SHORT;

            System.out.println(result.responseCode);
            System.out.println(result.getBody());

            if (result.responseCode < 200 || result.responseCode >= 300) {
                Toast toast = Toast.makeText(context, "Event Call Failed!", duration);
                toast.show();
            } else {
                try {
                    JSONArray eventsArray = result.getJson().getJSONArray("data");
                    FamilyModel familyModel = getFamilyModel(personsArray, eventsArray, personId);
                    ((MainActivity) context).showMapFragment(familyModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ServiceCallResult {
        private int responseCode;
        private String body;

        ServiceCallResult(int responseCode, String body) {
            this.responseCode = responseCode;
            this.body = body;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getBody() {
            return body;
        }

        public JSONObject getJson() {
            JSONObject reader = null;

            try {
                reader = new JSONObject(body);

                if (reader.has("data")) {
                    JSONArray dataArray = new JSONArray(reader.getString("data"));
                    reader.put("data", dataArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return reader;
        }
    }

    public class HttpResponse {
        public int responseCode;
        public String response;

        public HttpResponse(int responseCode, String response) {
            this.responseCode = responseCode;
            this.response = response;
        }
    }

    private FamilyModel getFamilyModel(JSONArray personsArray, JSONArray eventsArray, String personId) {
        FamilyModel familyModel = new FamilyModel();
        familyModel.setCurrentUser(personId);

        try {
            List<FamilyModel.Person> persons = familyModel.getPersons();
            for (int i = 0; i < personsArray.length(); i++) {
                JSONObject personObject = personsArray.getJSONObject(i);
                FamilyModel.Person person = new FamilyModel.Person(
                        personObject.getString("personID"),
                        personObject.getString("firstName"),
                        personObject.getString("lastName"),
                        personObject.getString("gender")
                );
                person.setDescendant(personObject.getString("descendant"));
                person.setFather(personObject.getString("father"));
                person.setMother(personObject.getString("mother"));
                person.setSpouse(personObject.getString("spouse"));

                familyModel.addPerson(person);
            }

            List<FamilyModel.Event> events = familyModel.getEvents();
            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject eventObject = eventsArray.getJSONObject(i);
                FamilyModel.Event event = new FamilyModel.Event(
                        eventObject.getString("eventID"),
                        eventObject.getString("descendant"),
                        eventObject.getString("personID"),
                        eventObject.getDouble("latitude"),
                        eventObject.getDouble("longitude"),
                        eventObject.getString("country"),
                        eventObject.getString("city"),
                        eventObject.getString("eventType"),
                        eventObject.getInt("year")
                );

                familyModel.addEvent(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return familyModel;
    }

}
