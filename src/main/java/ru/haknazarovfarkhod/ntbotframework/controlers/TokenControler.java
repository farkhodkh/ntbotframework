package ru.haknazarovfarkhod.ntbotframework.controlers;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import ru.haknazarovfarkhod.ntbotframework.application.ApplicationParameters;
public class TokenControler {
    public  static String mToken;

    public Date getAuthToken(Date tokenUpdateTime) {
        ApplicationParameters parameters = new ApplicationParameters();
        HttpRequesterControler httpRequester = new HttpRequesterControler();
        Date now = new Date();
        Calendar controlTime = Calendar.getInstance();
        controlTime.setTime(now);
        controlTime.add(Calendar.HOUR, -1);

        if (!(tokenUpdateTime==null)&&tokenUpdateTime.after(controlTime.getTime())) {
            return tokenUpdateTime;
        }

        String url = "https://login.microsoftonline.com/botframework.com/oauth2/v2.0/token";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Host", "login.microsoftonline.com");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String request = "grant_type=client_credentials&client_id=" + parameters.getAPP_ID() + "&client_secret=" + parameters.getAPP_SECRET() + "&scope=https%3A%2F%2Fapi.botframework.com%2F.default";

        try {
            System.out.println("\nGetting new token");
            String response = httpRequester.sendPost(url, request, headers);
            JSONObject token_response = new JSONObject(response);

            System.out.println("\nToken refreshed");
            mToken = token_response.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
}
