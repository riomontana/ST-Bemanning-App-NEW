package com.stbemanning.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.stbemanning.api.Api;
import com.stbemanning.api.ApiListener;
import com.stbemanning.api.PerformNetworkRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterTokenTask extends AsyncTask<String, Void, String> implements ApiListener {

    private String appToken;
    private String userId;

    @Override
    protected String doInBackground(String... strings) {
        this.userId = strings[0];
        this.appToken = strings[1];
        getToken(userId);
        return null;
    }

    public void getToken(String userId){
        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", userId);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_APP_TOKEN, params, this);
        request.execute();
        Log.d("GET_APP_TOKEN","NetworkRequest executed");
    }

    public void updateToken(String userId, String appToken){
        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("token", appToken);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_APP_TOKEN, params, this);
        request.execute();
        Log.d("UPPDATE_APP_TOKEN","NetworkRequest executed");
    }

    public void addToken(String userId, String appToken){
        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", userId);
        params.put("token", appToken);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_ADD_APP_TOKEN, params, this);
        request.execute();
        Log.d("ADD_APP_TOKEN","NetworkRequest executed");
    }

    @Override
    public void apiResponse(JSONObject response) {
        Boolean tokenIsUpdated = false;
        try {
            JSONArray jsonArray = response.getJSONArray("token");
            if(!(jsonArray.length() == 0)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.get(i).toString().equals("{\"token\":\"" + appToken + "\"}")) {
                        Log.d("API_RESPONS", "Token is up to date");
                        tokenIsUpdated = true;
                    }
                }
                if (!tokenIsUpdated) {
                    Log.d("API_RESPONS", "Token exist but is not up to date");
                    updateToken(userId, appToken);
                }
            }else {
                Log.d("API_RESPONS","No token whith this userId");
                addToken(userId, appToken);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
