package ru.haknazarovfarkhod.ntbotframework.application;

import ru.haknazarovfarkhod.ntbotframework.controlers.HttpRequesterControler;
import ru.haknazarovfarkhod.ntbotframework.controlers.MessageControler;
import ru.haknazarovfarkhod.ntbotframework.controlers.TokenControler;
import ru.haknazarovfarkhod.ntbotframework.Greeting;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RequestMapper {
    protected static Date tokenUpdateTime;
    TokenControler tokenControler = new TokenControler();
    HttpRequesterControler httpRequester = new HttpRequesterControler();
    MessageControler messageControter = new MessageControler();

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method = RequestMethod.GET, value = "*")
    public Greeting greetingAll(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println("Received Message: " + name);
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(method = RequestMethod.POST, value = "*")
    public ResponseEntity<?> messageReceivedAll(@RequestBody String request) {
        System.out.println("Received Message: " + request.toString());

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/message")
    public ResponseEntity<?> messageReceived(@RequestBody String request) throws UnsupportedEncodingException {
        this.tokenUpdateTime = tokenControler.getAuthToken(this.tokenUpdateTime);
        System.out.println("Received Message: " + request.toString());
        JSONObject json = new JSONObject(request);

        String conversationType = (String) json.get("type");

        if (conversationType.equals("contactRelationUpdate")) {
            messageControter.sendMessage((String) json.get("serviceUrl"), json.getJSONObject("conversation"), json.getJSONObject("from"), "Hello, new user!", json.getJSONObject("recipient"), (String) json.get("id"), json);
        } else if (conversationType.equals("message")) {
            try {
                if (json.getString("text").contains("ntbotframework")) {
                    messageControter.sendMessage((String) json.get("serviceUrl"), json.getJSONObject("conversation"), json.getJSONObject("from"), "Hello, World!", json.getJSONObject("recipient"), (String) json.get("id"), json);
                } else {
                    //TODO Убрать кириллицу
                    String inputString = "йцукен";
                    String outputText = new String(inputString.getBytes(), "Cyrillic");
                    System.out.println("inputString:  " + inputString);
                    System.out.println("outputText: " + outputText);
                    messageControter.sendMessage((String) json.get("serviceUrl"), json.getJSONObject("conversation"), json.getJSONObject("from"), inputString, json.getJSONObject("recipient"), (String) json.get("id"), json);
                }
            } catch (Exception e) {
                e.printStackTrace();
                messageControter.sendMessage((String) json.get("serviceUrl"), json.getJSONObject("conversation"), json.getJSONObject("from"), "Дай мне по башке у меня сбой какойто", json.getJSONObject("recipient"), (String) json.get("id"), json);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

}