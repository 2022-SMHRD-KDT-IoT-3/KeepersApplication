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

    private ListView board_List;
        // private ArrayAdapter<String> adapter;
    private BoardAdapter adapter = new BoardAdapter();
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        board_List = findViewById(R.id.board_list);
         // adapter.addItem(1,"test","test","test");
         // board_List.setAdapter(adapter);
         send_boardList_Request();
    }
    private void send_boardList_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        String url = "http://59.0.236.112:8081/keepers/andBoardList.do";

        //stringRequest -> 요청시 필요한 문자열 객체
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("resultValue",response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i< jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        int b_seq = jsonObject.getInt("b_seq");
                        String b_title = jsonObject.getString("b_title");
                        String b_content = jsonObject.getString("b_content");
                        String b_id = jsonObject.getString("b_id");
                        String b_signdate = jsonObject.getString("b_signdate");

                        adapter.addItem(b_seq, b_title, b_id, b_signdate);
                        // adapter.addItem(1, "테스트", "테스트", "테스트");

                        //items.add(b_seq);
                        // items.add(b_title);
                        // items.add(b_content);
                        // items.add(b_id);
                        // items.add(b_signdate);
                    }

                    // adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);



//                    adapter.addItem(1,"test","test","test");
//
                    board_List.setAdapter(adapter);
                    // jsonObject에는 회원들의 정보가 담겨있다.
                    // 회원들의 정보를 리스트뷰에 보이게 하시오

                    adapter.notifyDataSetChanged();

                    board_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            BoardVO vo = (BoardVO) adapterView.getItemAtPosition(i);
                            Toast.makeText(getApplicationContext(), vo.toString(), Toast.LENGTH_SHORT).show();


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
        }){
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

                // String c_manager_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
                // params.put("c_manager_id", c_manager_id);

                return params;
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }
}