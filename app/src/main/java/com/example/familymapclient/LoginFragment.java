package com.example.familymapclient;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher, RadioGroup.OnCheckedChangeListener {
    private View fragmentView;
    private ServerProxy serverProxy;

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
        serverProxy = new ServerProxy(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.login_fragment, container, false);

        this.fragmentView = v;

        Button b = v.findViewById(R.id.registerButton);
        b.setEnabled(false);
        b.setOnClickListener(this);

        b = v.findViewById(R.id.signInButton);
        b.setEnabled(false);
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

        RadioGroup radioGroup = fragmentView.findViewById(R.id.gender);
        radioGroup.setOnCheckedChangeListener(this);

        afterTextChanged(null);

        return v;
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
                onRegisterButtonClicked();
                break;
            case R.id.signInButton:
                onSigninButtonClicked();
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

        RadioGroup radioGroup = fragmentView.findViewById(R.id.gender);
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            registerEnabled = false;
        }

        signInButton.setEnabled(signInEnabled);
        registerButton.setEnabled(registerEnabled);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        afterTextChanged(null);
    }

    void onSigninButtonClicked() {
        String serverHost = ((TextView) fragmentView.findViewById(R.id.serverHost)).getText().toString();
        String serverPort = ((TextView) fragmentView.findViewById(R.id.serverPort)).getText().toString();
        String username = ((TextView) fragmentView.findViewById(R.id.userName)).getText().toString();
        String password = ((TextView) fragmentView.findViewById(R.id.password)).getText().toString();

        serverProxy.login(serverHost, serverPort, username, password);
    }

    void onRegisterButtonClicked() {
        String serverHost = ((TextView) fragmentView.findViewById(R.id.serverHost)).getText().toString();
        String serverPort = ((TextView) fragmentView.findViewById(R.id.serverPort)).getText().toString();
        String userName = ((TextView) fragmentView.findViewById(R.id.userName)).getText().toString();
        String password = ((TextView) fragmentView.findViewById(R.id.password)).getText().toString();
        String firstName = ((TextView) fragmentView.findViewById(R.id.firstName)).getText().toString();
        String lastName = ((TextView) fragmentView.findViewById(R.id.lastName)).getText().toString();
        String email = ((TextView) fragmentView.findViewById(R.id.email)).getText().toString();

        RadioGroup radioGroup = fragmentView.findViewById(R.id.gender);
        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton radioButton = fragmentView.findViewById(selectedId);
        String gender = radioButton.getText().toString().equals("Male") ? "m" : "f";

        serverProxy.register(serverHost, serverPort, userName, password, firstName, lastName, email, gender);
    }
}
