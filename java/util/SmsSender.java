package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

public class SmsSender {

    public static boolean sendSms(String phone, String message) throws Exception {
        // prefer Twilio when configured
        String twilioSid = System.getenv("TWILIO_SID");
        String twilioAuth = System.getenv("TWILIO_AUTH");
        String twilioFrom = System.getenv("TWILIO_FROM");
        if (twilioSid != null && !twilioSid.isEmpty() && twilioAuth != null && !twilioAuth.isEmpty() && twilioFrom != null && !twilioFrom.isEmpty()) {
            return sendViaTwilio(twilioSid, twilioAuth, twilioFrom, phone, message);
        }

        // fallback to Textbelt if configured
        String textbeltKey = System.getenv("TEXTBELT_KEY");
        if (textbeltKey == null || textbeltKey.isEmpty()) {
            // no provider configured
            return false;
        }
        return sendViaTextbelt(textbeltKey, phone, message);
    }

    private static boolean sendViaTwilio(String sid, String auth, String from, String to, String body) throws Exception {
        String urlStr = String.format("https://api.twilio.com/2010-04-01/Accounts/%s/Messages.json", sid);
        URI uri = new URI(urlStr);
        URL url = uri.toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        String creds = sid + ":" + auth;
        String basic = Base64.getEncoder().encodeToString(creds.getBytes("UTF-8"));
        con.setRequestProperty("Authorization", "Basic " + basic);
        con.setDoOutput(true);
        String postData = "To=" + encode(to) + "&From=" + encode(from) + "&Body=" + encode(body);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postData);
            wr.flush();
        }
        int rc = con.getResponseCode();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(rc >= 400 ? con.getErrorStream() : con.getInputStream()))) {
            String inputLine;
            StringBuilder resp = new StringBuilder();
            while ((inputLine = in.readLine()) != null) resp.append(inputLine);
            return rc >= 200 && rc < 300;
        }
    }

    private static boolean sendViaTextbelt(String key, String phone, String message) throws Exception {
        String urlStr = "https://textbelt.com/text";
        URI uri = new URI(urlStr);
        URL url = uri.toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        String postData = "phone=" + encode(phone) + "&message=" + encode(message) + "&key=" + encode(key);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postData);
            wr.flush();
        }
        int rc = con.getResponseCode();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(rc >= 400 ? con.getErrorStream() : con.getInputStream()))) {
            String inputLine;
            StringBuilder resp = new StringBuilder();
            while ((inputLine = in.readLine()) != null) resp.append(inputLine);
            return resp.toString().contains("\"success\":true");
        }
    }

    private static String encode(String s) {
        try { return java.net.URLEncoder.encode(s, "UTF-8"); } catch (java.io.UnsupportedEncodingException e) { return s; }
    }
}
