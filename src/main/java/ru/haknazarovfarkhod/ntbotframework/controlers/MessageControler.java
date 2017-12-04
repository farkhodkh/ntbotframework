package ru.haknazarovfarkhod.ntbotframework.controlers;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageControler {

    TokenControler tokenControler = new TokenControler();
    HttpRequesterControler httpRequester = new HttpRequesterControler();

    public void sendMessage(String BaseURL, JSONObject conversation, JSONObject recepient, String text, JSONObject from, String conversionId, JSONObject json) throws UnsupportedEncodingException {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        char lastChar = BaseURL.charAt(BaseURL.length() - 1);
        if (!(Character.toLowerCase((char) lastChar) == '/')) {
            BaseURL = BaseURL + "/";
        }
        String channelId = (String) json.get("channelId");
        String url = BaseURL + "v3/conversations/" + conversation.get("id") + "/activities";

        if (!channelId.equals("skype") && (!channelId.equals("webchat"))) {
            url += '/' + conversionId;
        }
        System.out.println("Conversation id is:" + conversionId);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + tokenControler.getmToken());
        headers.put("User-Agent", "Mozilla/5.0");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        //headers.put("Content-Type", "application/json; charset=Cp1251");
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("type", "message");
        request.put("conversation", conversation);
        request.put("timestamp", dateFormat.format(now));
        request.put("channelId", channelId);
        request.put("serviceUrl", json.get("serviceUrl"));
        request.put("recipient", recepient);
        request.put("locale", "ru-RU");
        request.put("from", from);
        request.put("replyToId", conversionId);
        request.put("text", text);

        try {
            JSONObject jsonRequest = new JSONObject(request);
            String response = httpRequester.sendPost(url, jsonRequest.toString(), headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
