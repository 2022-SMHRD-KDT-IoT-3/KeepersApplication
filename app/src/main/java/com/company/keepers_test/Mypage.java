package com.company.keepers_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

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

public class Mypage extends AppCompatActivity {

    // 필요한 객체 생성
    private TextView tv_myid, tv_myname, tv_myphone, tv_myemail;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    String m_id = "";
    String m_pw = "";
    String m_name = "";
    String m_phone = "";
    String m_email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        tv_myid = findViewById(R.id.tv_myid);
        tv_myname = findViewById(R.id.tv_myname);
        tv_myphone = findViewById(R.id.tv_myphone);
        tv_myemail = findViewById(R.id.tv_myemail);

        // 스프링 내 정보 조회 요청
        send_MyInfoSelect_Request();

    }

    private void send_MyInfoSelect_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        String url = "http://211.63.240.71:8081/keepers/andMyInfoSelect.do";

        //stringRequest -> 요청시 필요한 문자열 객체
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);

                try {
                    // 스프링에서 넘어온 jsonObect에 담긴 데이터를 변수에 담기
                    JSONObject jsonObject = new JSONObject(response);
                    m_id = jsonObject.getString("m_id");
                    m_pw = jsonObject.getString("m_pw");
                    m_name = jsonObject.getString("m_name");
                    m_phone = jsonObject.getString("m_phone");
                    m_email = jsonObject.getString("m_email");


                    // 받아온 데이터로 텍스트뷰 내용 변경
                    tv_myid.setText(m_id);
                    tv_myname.setText(m_name);
                    tv_myphone.setText(m_phone);
                    tv_myemail.setText(m_email);

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

                // 스프링으로 로그인 한 아이디 정보 전송
                String c_manager_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
                Log.v("Test", "로그인아이디" + c_manager_id);
                params.put("m_id", c_manager_id);

                return params;
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }
}