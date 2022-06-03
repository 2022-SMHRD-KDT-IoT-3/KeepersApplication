package com.company.keepers_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CareSelect extends AppCompatActivity {

    // 필요한 객체 선언
    private ImageView iv_back;
    private TextView tv_info, tv_info2, tv_info3;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    // 마지막 활동시간을 담을 변수 lastAct
    private String lastAct = "";
    // 생활반응에 맞는 이미지를 나타낼 iv_result
    private ImageView iv_result;
    // 이미지를 담고 있는 imgArray
    private int[] imgArray = {R.drawable.on, R.drawable.off};

    k_careVO vo = new k_careVO();
    ArrayList<ValueVO> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_select);

        tv_info = findViewById(R.id.tv_info);
        tv_info2 = findViewById(R.id.tv_info2);
        tv_info3 = findViewById(R.id.tv_info3);
        iv_result = findViewById(R.id.iv_result);
        iv_back = findViewById(R.id.iv_back2);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 인텐트 처리
        Intent intent = getIntent();
        vo = (k_careVO) intent.getSerializableExtra("vo");
        Log.v("Test", vo.toString());

        // 스프링 활동중 체크 요청
        send_andMonitorAct_Request();
    }

    private void send_andMonitorAct_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        String url = getString(R.string.KeepersIP) +"/andMonitoringAct.do";

        //stringRequest -> 요청시 필요한 문자열 객체
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 응답데이터를 받아오는 곳
            @Override
            public void onResponse(String response) {
                Log.v("Test", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String v_weight = jsonObject.getString("v_weight");
                        String v_signdate = jsonObject.getString("v_signdate");
                        String v_bat = jsonObject.getString("v_bat");
                        // 스프링에서 넘어온 v_weight와 v_signdate를 ValueVo에 담기
                        ValueVO valueVO = new ValueVO(v_weight, v_signdate, v_bat);
                        // Log.v("Test", valueVO.toString());
                        // ValueVo를 어레이리스트에 담기
                        items.add(valueVO);
                    }
                    // Log.v("Test", String.valueOf(items));

                    // 어레이리스트에 담긴 v_weight를 담을 배열 생성
                    Double[] result_weight = new Double[items.size()];
                    int[] result_bat = new int[items.size()];


                    for (int i = 0; i < items.size(); i++) {
                        // v_weight 담기
                        result_weight[i] = Double.parseDouble(items.get(i).getV_weight());
                        result_bat[i] = Integer.parseInt(items.get(i).getV_bat());
                        // Log.v("Test", items.get(i).getV_weight() + "" + i);

                        // 무게 데이터 값이 10보다 클 경우
                        // 해당 시간 값을 lastAct에 담기
                        if (Double.parseDouble(items.get(i).getV_weight()) > 10) {
                            lastAct = items.get(i).getV_signdate();
                            break;
                        }
                    }

                    String lastBat = String.valueOf(result_bat[0]);

                    // 무게 데이터 최신 값이 10보다 클 경우
                    // 활동중으로 텍스트 변경, 이미지 변경
                    if (result_weight[0] > 10) {
                        tv_info2.setText("활동중");
                        iv_result.setImageResource(imgArray[0]);


                        // 무게 데이터 최신 값이 10보다 작을 경우
                        // 활동중으로 텍스트 변경, 이미지 변경
                    } else {
                        tv_info2.setText("무반응");
                        iv_result.setImageResource(imgArray[1]);
                    }

                    // Log.v("Test", lastAct);

                    // result_bat 값으로 텍스트 변경
                    tv_info3.setText(lastBat+"%");

                    // lastAct 값으로 텍스트 변경
                    tv_info.setText(lastAct.toString());

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
                // 인텐트에서 넘어온 vo에 담긴 C_seq를 스프링으로 전송
                params.put("d_c_seq", String.valueOf(vo.getC_seq()));
                return params;
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }

}