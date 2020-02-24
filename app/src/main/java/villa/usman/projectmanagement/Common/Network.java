package villa.usman.projectmanagement.Common;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import villa.usman.projectmanagement.DTO.ResponseListner;

public class Network {
    private static final String TAG = "Network";
    private static Network instance = null;
    public RequestQueue queue;

    private Network(Context context)
    {
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized Network getInstance(Context context)
    {
        if (null == instance)
            instance = new Network(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized Network getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(Network.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void CallJSONRequestPOSTAPI(String url, JSONObject postparams) {
        ResponseListner.object = new JSONObject();
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                Log.d(TAG + ": ", "somePostRequest Response : " + response.toString());
                                if(null != response.toString())

                                    ResponseListner.object = response;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                    JSONObject err = new JSONObject();
                    try {
                        err.put("ResponseCode", Constants.Error);
                    }catch (JSONException e){
                        Log.d(TAG,e.getMessage());
                    }
                    ResponseListner.object =err;
                }
            }) {
            };
            queue.add(jsonRequest);
    }

}
