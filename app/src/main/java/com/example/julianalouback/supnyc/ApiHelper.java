    package com.example.julianalouback.supnyc;

    import com.android.volley.Response;
    import com.android.volley.RequestQueue;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.JsonArrayRequest;
    import com.android.volley.toolbox.Volley;
    import com.example.julianalouback.supnyc.Models.Event;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.List;

    /**
     * Created by julianalouback on 12/9/14.
     */
    public class ApiHelper {

        private List<Event> events = new ArrayList<Event>();

        public List<Event> getEvents(String type) {

            String url = "" + type;

            RequestQueue rq = Volley.newRequestQueue(this);
            JsonArrayRequest jReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            jsonToEvent(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error

                }
            });
            rq.add(jReq);
        }

        public void jsonToEvent(JSONArray jsonEvents){
            Event event;
            if (jsonEvents != null) {
                for (int i=0;i<jsonEvents.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = jsonEvents.getJSONObject(i);
                        event = new Event(jsonObject.getString("title"),jsonObject.getString("description"),
                                jsonObject.getString("address"),jsonObject.getString("host_username"),
                                Long.parseLong(jsonObject.getString("start")),Long.parseLong(jsonObject.getString("end")),
                                jsonObject.getString("type"),jsonObject.getString("image_url"));
                        this.events.add(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }
