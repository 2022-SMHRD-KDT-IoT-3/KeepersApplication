package com.company.keepers_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class CareSelect extends AppCompatActivity {

    private TextView tv_info;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_select);

        tv_info = findViewById(R.id.tv_info);

        send_ValueSelect_Request();
    }

    private void send_ValueSelect_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        // String url = "http://211.63.240.71:8081/keepers/careList.do";
        String url = "http://59.0.236.112:8081/keepers/andValueSelect.do";

        //stringRequest -> 요청시 필요한 문자열 객체
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String c_name = jsonObject.getString("c_name");
//                            String c_birth = jsonObject.getString("c_birth");
//                            String c_phone = jsonObject.getString("c_phone");
//                            String c_address = jsonObject.getString("c_address");
//                            String c_memo = jsonObject.getString("c_memo");

                        // adapter.addItem(c_name, c_phone, c_address, c_memo);

                    }

                    // carelist_customview.setAdapter(adapter);

                    // adapter.notifyDataSetChanged();


                    // adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);

                    // CareList.setAdapter(adapter);
                    // jsonObject에는 회원들의 정보가 담겨있다.
                    // 회원들의 정보를 리스트뷰에 보이게 하시오


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            // 서버와의 연동 에러시 출력
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override //response를 UTF8로 변경해주는 소스코드
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String utf8String = new String(response.data, "UTF-8");
                    return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    // log error
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    // log error
                    return Response.error(new ParseError(e));
                }
            }

            // 보낼 데이터를 저장하는 곳
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                String c_manager_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
                params.put("c_manager_id", c_manager_id);

                return params;
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }
}