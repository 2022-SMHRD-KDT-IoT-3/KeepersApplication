package com.company.keepers_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CareList extends AppCompatActivity {

    // 필요한 객체 선언
    private ListView carelist_customview;
    private CareAdapter adapter = new CareAdapter();
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_list);

        carelist_customview = findViewById(R.id.carelist_customview);

        // 스프링 사용자 리스트 요청
        send_CareList_Request();
    }

    private void send_CareList_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        // String url = "http://211.63.240.71:8081/keepers/careList.do";
//         String url = "http://211.63.240.71:8081/keepers/andCareList.do";
        String url = "http://211.63.240.71:8081/keepers/andCareList.do";

        //stringRequest -> 요청시 필요한 문자열 객체
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);

                // 1. 받아온 JsonArray를 jsonObject로 변환
                // 2. 각각의 변수에 다시 담기
                // 3. 커스텀뷰 어댑터에 다시 담기

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String c_name = jsonObject.getString("c_name");
                        int c_seq = Integer.parseInt(jsonObject.getString("c_seq"));
                        String c_phone = jsonObject.getString("c_phone");
                        String c_address = jsonObject.getString("c_address");
                        String c_memo = jsonObject.getString("c_memo");

                        adapter.addItem(c_seq, c_name, c_phone, c_address, c_memo);

                    }

                    // 어댑터에 담긴 데이터를 게시판리스트 커스텀뷰에 세팅
                    carelist_customview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    // 커스텀뷰 클릭시 이벤트 처리
                    carelist_customview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // 클릭한 정보 vo에 담기
                            k_careVO vo = (k_careVO) adapterView.getItemAtPosition(i);
                            // Toast.makeText(getApplicationContext(), vo.toString(), Toast.LENGTH_SHORT).show();
                            // 인텐트에 vo를 담아서 관리대상 선택(모니터링) 페이지로 이동
                            Intent intent = new Intent(getApplicationContext(), CareSelect.class);
                            intent.putExtra("vo", vo);
                            startActivity(intent);

                        }
                    });

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
                // 로그인 한 회원의 아이디 정보를 스프링으로 전송
                String c_manager_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
                params.put("c_manager_id", c_manager_id);

                return params;
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }
}