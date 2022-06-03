package com.company.keepers_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class BoardList extends AppCompatActivity {

    // 필요한 객체 선언
    private ListView board_List;
    private BoardAdapter adapter = new BoardAdapter();
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    //a
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        board_List = findViewById(R.id.carelist_customview);

        // 스프링 게시판 목록 조회 요청
        send_boardList_Request();
    }

    private void send_boardList_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        String url = "http://211.63.240.71:8081/keepers/andBoardList.do";

        //stringRequest -> 요청시 필요한 문자열 객체
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue", response);

                // 1. 받아온 JsonArray를 jsonObject로 변환
                // 2. 각각의 변수에 다시 담기
                // 3. 보드리스트 커스텀뷰 어댑터에 다시 담기

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        int b_seq = jsonObject.getInt("b_seq");
                        String b_title = jsonObject.getString("b_title");
                        // 작성 내용은 게시판 리스트에서 보여주지 않기 때문에 처리하지 않음
                        // String b_content = jsonObject.getString("b_content");
                        String b_id = jsonObject.getString("b_id");
                        String b_signdate = jsonObject.getString("b_signdate");

                        adapter.addItem(b_seq, b_title, b_id, b_signdate);
                    }

                    // 어댑터에 담긴 데이터를 게시판리스트 커스텀뷰에 세팅
                    board_List.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    // 커스텀뷰 클릭시 이벤트 처리
                    board_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            // 클릭한 정보 vo에 담기
                            BoardVO vo = (BoardVO) adapterView.getItemAtPosition(i);
                            // 인텐트에 vo를 담아서 게시글 내용 보기 페이지로 이동
                            Intent intent = new Intent(getApplicationContext(), BoardSelect.class);
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
                return params;
                // 게시판 목록 조회는 스프링에 보내줄 데이터가 없음
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }
}