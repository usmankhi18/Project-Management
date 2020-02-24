package villa.usman.projectmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import es.dmoral.toasty.Toasty;
import villa.usman.projectmanagement.Common.CommonMethods;
import villa.usman.projectmanagement.Common.Constants;
import villa.usman.projectmanagement.Common.Network;

import static villa.usman.projectmanagement.DTO.ResponseListner.object;

public class LoginActivity extends Activity {

    Button btnLogin,btnNext;
    EditText et_Email,et_Password;
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    static boolean ok,called;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Network.getInstance(this);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        et_Email = (EditText)findViewById(R.id.et_email);
        et_Password = (EditText)findViewById(R.id.et_password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        sharedpreferences = getSharedPreferences(Constants.ProfilePREFERENCES, Context.MODE_PRIVATE);

        btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    VerifyUser(et_Email.getText().toString(),getApplicationContext());
                    ok = false;
                    if(called) {
                        Waiting(ok);
                    }else{
                        progressBar.setVisibility(View.GONE);
                    }
                }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void SaveResponse(boolean isSave){
        Handler handler;
        if(!object.toString().equalsIgnoreCase("{}")) {
            try {
                try {
                    progressBar.setVisibility(View.GONE);
                    String responseCode = object.getString("ResponseCode");
                    String responseMessage = object.getString("ResponseMessage");
                    if (responseCode.equalsIgnoreCase("00")) {
                        JSONObject responseData = object.getJSONObject("ResponseData");
                        JSONObject LoginResponse = responseData.getJSONObject("LoginResp");
                        int UserID = LoginResponse.getInt("UserID");
                        int RoleID = LoginResponse.getInt("RoleID");
                        String FullName = LoginResponse.getString("FullName");
                        String RoleName = LoginResponse.getString("RoleName");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("UserID", String.valueOf(UserID));
                        editor.putString("RoleID", String.valueOf(RoleID));
                        editor.putString("FullName", FullName);
                        editor.putString("RoleName", RoleName);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toasty.error(getApplicationContext(), responseMessage, Toasty.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.d("Error", e.getMessage());
                }
            } catch (Exception e) {

            }
        }else{
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SaveResponse(false);
                }},3000);
        }
    }

    private void Waiting(boolean ok){
        Handler handler;
        if(!object.toString().equalsIgnoreCase("{}")) {
            try {
                String response = object.getString("ResponseCode");
                String message = object.getString("ResponseMessage");
                if(response.equalsIgnoreCase("00") && message.equalsIgnoreCase(Constants.Success)) {
                    ok = true;
                }else if(response.equalsIgnoreCase("00") && !message.equalsIgnoreCase(Constants.Success)){
                    Toasty.error(getApplicationContext(),Constants.NoData,Toasty.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                }
            }catch(Exception e){
            }
        }else{
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Waiting(false);
                }},3000);
        }
        if (ok) {
            btnNext.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            et_Email.setVisibility(View.GONE);
            et_Password.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private static synchronized void VerifyUser(String email,final Context context){
        try{
            if(email.isEmpty()){
                Toasty.warning(context,"Please Enter Email",Toasty.LENGTH_LONG).show();
            }
            else if (new CommonMethods().isEmail(email) == false) {
                Toasty.warning(context,"Enter valid email!",Toasty.LENGTH_LONG).show();
            }else{
                if (new CommonMethods().isOnline(context)) {
                    CallVerifyUserAPI(email,context);
                } else {
                    Toasty.error(context, "Internet Not Connected", Toasty.LENGTH_LONG).show();
                }
            }
        }catch (Exception ex){
            Toasty.error(context,ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
    }

    private static synchronized void CallVerifyUserAPI(String email,final Context context){
        try{
            String url = Constants.UserExistCall;
            JSONObject postparams = new JSONObject();
            JSONObject postcredentials = new JSONObject();
            postcredentials.put("APIUserName", Constants.APIUserName);
            postcredentials.put("APIPassword", Constants.APIPassword);
            postparams.put("APICredentials",postcredentials);
            postparams.put("Email", email);
            called = true;
            Network.getInstance().CallJSONRequestPOSTAPI(url, postparams);
        }catch(Exception ex){
            Toasty.error(context,ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
    }

    private void CallLoginAPI(){
        try{
            progressBar.setVisibility(View.VISIBLE);

            String url = Constants.LoginCall;
            JSONObject postparams = new JSONObject();
            JSONObject postcredentials = new JSONObject();
            postcredentials.put("APIUserName", Constants.APIUserName);
            postcredentials.put("APIPassword", Constants.APIPassword);
            postparams.put("APICredentials",postcredentials);
            postparams.put("Email", et_Email.getText());
            postparams.put("Password", et_Password.getText());
            Network.getInstance().CallJSONRequestPOSTAPI(url, postparams);
            SaveResponse(false);
        }catch(Exception ex){
            Toasty.error(getApplicationContext(),ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
    }

    private void Login(){
        try{
            if(et_Password.getText().toString().isEmpty()){
                Toasty.warning(getApplicationContext(),"Please Enter Password",Toasty.LENGTH_LONG).show();
                return;
            }else{
                if (new CommonMethods().isOnline(getApplicationContext())) {
                    CallLoginAPI();
                } else {
                    Toasty.error(getApplicationContext(), "Internet Not Connected", Toasty.LENGTH_LONG).show();
                }
            }
        }catch (Exception ex) {
            Toasty.error(getApplicationContext(), ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }
}
