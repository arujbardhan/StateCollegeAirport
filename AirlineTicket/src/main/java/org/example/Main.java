import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class AirlineTicketFinder {
    private static final String OPENAI_API_KEY = "YOUR_API_KEY"; // Replace with your OpenAI API key
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        String userInput = "Find me the cheapest flight from New York to Los Angeles on July 15, 2024.";
        String response = getFlightDetails(userInput);
        System.out.println("Response from OpenAI: " + response);
    }

    private static String getFlightDetails(String userInput) {
        OkHttpClient client = new OkHttpClient();

        // Create the JSON request body
        JSONObject json = new JSONObject();
        json.put("model", "gpt-3.5-turbo");
        json.put("messages", new JSONArray().put(new JSONObject()
                .put("role", "user")
                .put("content", userInput)));

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

        // Build the request
        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            // Extracting the assistant's message
            String assistantResponse = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
            return assistantResponse;
        } catch (IOException e)      {
            e.printStackTrace();
            return null;
        }
    }
}
