package com.example.familymapclient;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private OnFragmentInteractionListener mListener;
    private View fragmentView;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        this.fragmentView = v;

        Button b = v.findViewById(R.id.registerButton);
        b.setOnClickListener(this);

        b = v.findViewById(R.id.signInButton);
        b.setOnClickListener(this);

        // add text changed listeners to enable/disable buttons
        EditText textField = fragmentView.findViewById(R.id.serverHost);
        textField.addTextChangedListener(this);

        textField = fragmentView.findViewById(R.id.serverPort);
        textField.addTextChangedListener(this);

        textField = fragmentView.findViewById(R.id.userName);
        textField.addTextChangedListener(this);

        textField = fragmentView.findViewById(R.id.password);
        textField.addTextChangedListener(this);

        textField = fragmentView.findViewById(R.id.firstName);
        textField.addTextChangedListener(this);

        textField = fragmentView.findViewById(R.id.lastName);
        textField.addTextChangedListener(this);

        textField = fragmentView.findViewById(R.id.email);
        textField.addTextChangedListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                onRegisterButtonClicked(v);
                break;
            case R.id.signInButton:
                onSigninButtonClicked(v);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Button signInButton = fragmentView.findViewById(R.id.signInButton);
        Button registerButton = fragmentView.findViewById(R.id.registerButton);

        boolean signInEnabled = true;
        boolean registerEnabled = true;

        EditText textField = fragmentView.findViewById(R.id.serverHost);
        if (textField.getText().toString().equals("")) {
            signInEnabled = false;
            registerEnabled = false;
        }

        textField = fragmentView.findViewById(R.id.serverPort);
        if (textField.getText().toString().equals("")) {
            signInEnabled = false;
            registerEnabled = false;
        }

        textField = fragmentView.findViewById(R.id.userName);
        if (textField.getText().toString().equals("")) {
            signInEnabled = false;
            registerEnabled = false;
        }

        textField = fragmentView.findViewById(R.id.password);
        if (textField.getText().toString().equals("")) {
            signInEnabled = false;
            registerEnabled = false;
        }

        textField = fragmentView.findViewById(R.id.firstName);
        if (textField.getText().toString().equals("")) {
            registerEnabled = false;
        }

        textField = fragmentView.findViewById(R.id.lastName);
        if (textField.getText().toString().equals("")) {
            registerEnabled = false;
        }

        textField = fragmentView.findViewById(R.id.email);
        if (textField.getText().toString().equals("")) {
            registerEnabled = false;
        }

        signInButton.setEnabled(signInEnabled);
        registerButton.setEnabled(registerEnabled);
    }

    void onSigninButtonClicked(View v) {
        new SigninOperation(fragmentView).execute();
    }

    void onRegisterButtonClicked(View v) {
        new RegisterOperation(fragmentView).execute();
    }

    private class SigninOperation extends AsyncTask<Void, Void, ServiceCallResult> {

        private View view;

        SigninOperation(View view) {
            this.view = view;
        }

        @Override
        protected ServiceCallResult doInBackground(Void... params) {

            String serverHost = ((TextView) view.findViewById(R.id.serverHost)).getText().toString();
            String serverPort = ((TextView) view.findViewById(R.id.serverPort)).getText().toString();
            String userName = ((TextView) view.findViewById(R.id.userName)).getText().toString();
            String password = ((TextView) view.findViewById(R.id.password)).getText().toString();

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes("{\n" +
                        "\"userName\": \"" + userName +"\",\n" +
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

                return new ServiceCallResult(responseCode, response.toString());
            } catch (MalformedURLException mue) {
                return new ServiceCallResult(500, mue.getMessage());
            } catch (IOException ioe) {
                return new ServiceCallResult(500, ioe.getMessage());
            }
        }

        protected void onPostExecute(ServiceCallResult result) {
            if (result.responseCode < 200 || result.responseCode >= 300) {
                Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "Login Failed!", duration);
                toast.show();
            } else {
                new PersonOperation(view).execute(result);
            }
        }
    }

    private class RegisterOperation extends AsyncTask<Void, Void, ServiceCallResult> {

        private View view;

        RegisterOperation(View view) {
            this.view = view;
        }

        @Override
        protected ServiceCallResult doInBackground(Void... params) {

            String serverHost = ((TextView) view.findViewById(R.id.serverHost)).getText().toString();
            String serverPort = ((TextView) view.findViewById(R.id.serverPort)).getText().toString();
            String userName = ((TextView) view.findViewById(R.id.userName)).getText().toString();
            String password = ((TextView) view.findViewById(R.id.password)).getText().toString();
            String firstName = ((TextView) view.findViewById(R.id.firstName)).getText().toString();
            String lastName = ((TextView) view.findViewById(R.id.lastName)).getText().toString();
            String email = ((TextView) view.findViewById(R.id.email)).getText().toString();

            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.gender);
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
            String gender = radioButton.getText().toString().equals("Male") ? "m" : "f";

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("POST");

                con.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes("{\n" +
                        "\"userName\": \"" + userName +"\",\n" +
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

                return new ServiceCallResult(responseCode, response.toString());
            } catch (MalformedURLException mue) {
                return new ServiceCallResult(500, mue.getMessage());
            } catch (IOException ioe) {
                return new ServiceCallResult(500, ioe.getMessage());
            }
        }

        protected void onPostExecute(ServiceCallResult result) {
            if (result.responseCode < 200 || result.responseCode >= 300) {
                Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, "Register Failed!", duration);
                toast.show();
            } else {
                new PersonOperation(view).execute(result);
            }
        }
    }

    private class PersonOperation extends AsyncTask<ServiceCallResult, Void, ServiceCallResult> {

        private View view;

        PersonOperation(View view) {
            this.view = view;
        }

        @Override
        protected ServiceCallResult doInBackground(ServiceCallResult... params) {

            ServiceCallResult result = params[0];

            String serverHost = ((TextView) view.findViewById(R.id.serverHost)).getText().toString();
            String serverPort = ((TextView) view.findViewById(R.id.serverPort)).getText().toString();

            try {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" + result.getJson().getString("personID"));

                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                con.setRequestMethod("GET");

                con.setRequestProperty("authorization", result.getJson().getString("authToken"));

                int responseCode = con.getResponseCode();
                System.out.println("Response Code : " + responseCode);

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
            Context context = getActivity();
            int duration = Toast.LENGTH_SHORT;

            if (result.responseCode < 200 || result.responseCode >= 300) {
                Toast toast = Toast.makeText(context, "Person Call Failed!", duration);
                toast.show();
            } else {
                try {
                    String data = result.getJson().getString("data");
                    JSONObject jsonData = new JSONObject(data);
                    Toast toast = Toast.makeText(context, jsonData.getString("firstName") + " " + jsonData.getString("lastName"), duration);
                    toast.show();
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return reader;
        }
    }
}
