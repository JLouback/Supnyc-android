package com.example.julianalouback.supnyc.util;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.julianalouback.supnyc.AppController;
import com.example.julianalouback.supnyc.Models.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moldy530 on 12/22/14.
 */
public class APIHelper {

    public static void updateLike(final Event event, final String action, final boolean undo){
        String url = "http://supnyc.elasticbeanstalk.com/events_api";

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                //do error stuff
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                System.out.println(event.getType() + " " + event.getRangeKey() + " " + action);
                params.put("type", event.getType());
                params.put("range_key", event.getRangeKey());
                params.put("action", action);
                if(undo){
                    params.put("undo", "true");
                }
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(req);
    }
    public static void updateLike(final String range, final String type, final String action, final boolean undo){
        String url = "http://supnyc.elasticbeanstalk.com/events_api";

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                //do error stuff
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                //System.out.println(event.getType() + " " + event.getRangeKey() + " " + action);
                params.put("type", type);
                params.put("range_key", range);
                params.put("action", action);
                if(undo){
                    params.put("undo", "true");
                }
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(req);
    }
}
