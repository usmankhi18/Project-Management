package villa.usman.projectmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import villa.usman.projectmanagement.Common.Constants;

public class InsertTaskActivity extends Activity {
    TextView tv_username;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_task);
        sharedpreferences = getSharedPreferences(Constants.ProfilePREFERENCES, Context.MODE_PRIVATE);
        tv_username = (TextView)findViewById(R.id.tv_userName);
        tv_username.setText(sharedpreferences.getString("FullName","UserName"));
    }
}
