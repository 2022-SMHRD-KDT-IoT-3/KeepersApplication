package com.company.keepers_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class BoardSelect extends AppCompatActivity {

    // 객체 선언하기
    private TextView tv_select_title, tv_select_content, tv_select_id, tv_select_signdate;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    // 게시글 내용 담을 변수 생성
    String b_title = "";
    String b_content = "";
    String b_id = "";
    String b_signdate = "";
    BoardVO vo = new BoardVO();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_select);


        tv_select_title = findViewById(R.id.tv_select_title);
        tv_select_content = findViewById(R.id.tv_select_content);
        tv_select_id = findViewById(R.id.tv_select_id);
        tv_select_signdate = findViewById(R.id.tv_select_signdate);

        // 인텐트에 담긴 정보 받아오기
        Intent intent = getIntent();

        // 보드리스트에서 보낸 vo 객체를 겟시리얼라이저블 명령어를 통해 처리
        vo = (BoardVO) intent.getSerializableExtra("vo");
        Log.v("Test", vo.toString());

        // 스프링 게시글 선택 요청
        send_boardSelect_Request();

    }

    private void send_boardSelect_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        String url = "http://211.63.240.71:8081/keepers/andBoardSelect.do";

        //stringRequest -> 요청시 필요한 문자열 객체
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);

                // 1. 받아온 JsonArray를 jsonObject로 변환
                // 2. 각각의 변수에 다시 담기
                // 3. 텍스트뷰 setText

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        b_title = jsonObject.getString("b_title");
                        b_content = jsonObject.getString("b_content");
                        b_id = jsonObject.getString("b_id");
                        b_signdate = jsonObject.getString("b_signdate");
                    }

                    tv_select_title.setText(b_title);
                    tv_select_content.setText(b_content);
                    tv_select_id.setText(b_id);
                    tv_select_signdate.setText(b_signdate);


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
                // 인텐트에서 받아온 B_seq를 스프링으로 전송
                String b_seq = String.valueOf(vo.getB_seq());
                Log.v("Test", b_seq);
                params.put("b_seq", b_seq);

                return params;
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }
}
