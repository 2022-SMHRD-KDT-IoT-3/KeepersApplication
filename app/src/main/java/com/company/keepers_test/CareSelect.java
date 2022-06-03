package com.company.keepers_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CareSelect extends AppCompatActivity {

    // 필요한 객체 선언
    private TextView tv_info, tv_info2;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    // 마지막 활동시간을 담을 변수 lastAct
    private String lastAct = "";
    // 생활반응에 따른
    private ImageView iv_result;
    private int[] imgArray = {R.drawable.on, R.drawable.off};

    // private LineChart chart;
    // private ArrayList<Entry> chart_items = new ArrayList<>();

    k_careVO vo = new k_careVO();
    ArrayList<ValueVO> items = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_select);

        tv_info = findViewById(R.id.tv_info);
        tv_info2 = findViewById(R.id.tv_info2);
        iv_result = findViewById(R.id.iv_result);
        // chart = findViewById(R.id.chart);

        Intent intent = getIntent();
        vo = (k_careVO) intent.getSerializableExtra("vo");
        Log.v("Test", vo.toString());

        send_andMonitorAct_Request();
    }

    private void send_andMonitorAct_Request() {
// RequestQueue 객체 생성
        requestQueue = Volley.newRequestQueue(this); // 현재 어플 정보 넘겨주기 -> this또는 getApplicationContext()
        //서버에 요청할 주소
        // String url = "http://211.63.240.71:8081/keepers/careList.do";
        String url = "http://59.0.236.112:8081/keepers/andMonitoringAct.do";

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

//                        Float chart_weight = Float.parseFloat(jsonObject.getString("v_weight"));
//                        Float chart_signdate = Float.parseFloat(jsonObject.getString("v_signdate"));
//                        Log.v("Chart", String.valueOf(chart_signdate));
//                        Log.v("Chart", String.valueOf(chart_weight));

                        ValueVO valueVO = new ValueVO(v_weight, v_signdate);
                        Log.v("Test", valueVO.toString());
                        items.add(valueVO);
//                        chart_items.add(new Entry(chart_signdate, chart_weight));


                        // adapter.addItem(c_name, c_phone, c_address, c_memo);

                    }

//                    chart();

                    Log.v("Test", String.valueOf(items));

                    Double[] result_weight = new Double[items.size()];

                    for (int i = 0; i < items.size(); i++) {
                        result_weight[i] = Double.parseDouble(items.get(i).getV_weight());
                        Log.v("Test", items.get(i).getV_weight()+""+i);
                        if (Double.parseDouble(items.get(i).getV_weight()) > 10) {
                            Log.v("Test", items.get(i).getV_signdate() + "" + i);
                             lastAct = items.get(i).getV_signdate();
                             break;
                         }
                    }


                    if (result_weight[0] > 10) {
                        tv_info2.setText("활동중");
                        iv_result.setImageResource(imgArray[0]);


                    } else {
                        tv_info2.setText("무반응");
                        iv_result.setImageResource(imgArray[1]);
                    }

                    Log.v("Test", lastAct);
                    tv_info.setText(lastAct.toString());



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

                // String c_manager_id = SharedPreference.getAttribute(getApplicationContext(), "m_id");
                params.put("d_c_seq", String.valueOf(vo.getC_seq()));
                return params;
            }
        };
        stringRequest.setTag("main");
        requestQueue.add(stringRequest);
    }

//    public void chart() {
//
//        chart.setDragEnabled(true);
//        chart.setScaleEnabled(false);
//
//        LineDataSet set1 = new LineDataSet(chart_items, "Data Set 1");
//
//        set1.setFillAlpha(110);
//        set1.setColor(Color.BLUE);
//        set1.setLineWidth(5f);
//        set1.setValueTextSize(10f);
//        set1.setValueTextColor(Color.RED);
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1);
//
//        LineData data = new LineData(dataSets);
//        chart.setData(data);
//
//    }

}