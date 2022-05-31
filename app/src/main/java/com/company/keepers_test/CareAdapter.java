package com.company.keepers_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CareAdapter extends BaseAdapter {

    private ArrayList<k_careVO> items = new ArrayList<k_careVO>();

    public void addItem(String c_name, String c_phone, String c_address, String c_memo) {
        k_careVO vo = new k_careVO(c_name, c_phone, c_address, c_memo);
        items.add(vo);
    }


    @Override
    public int getCount() {
        // 어댑터가 가지고 있는 아이템의 개수를 알려주는 메소드
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        // 어댑터에게 해당 i 번째의 아이템을 요청하는 메소드
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // Adapter가 가지고 있는 item만큼 만들어놓은 xml에 틀에 맞게 넣어주는 메소드
        // 필수 구현

        // 1. kakao.xml 불러오기
        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.carelist_customlistview, viewGroup, false);
        }

        TextView tv_carelist_name = view.findViewById(R.id.tv_carelist_name);
        TextView tv_carelist_phone = view.findViewById(R.id.tv_carelist_phone);
        TextView tv_carelist_address = view.findViewById(R.id.tv_carelist_address);
        TextView tv_carelist_memo = view.findViewById(R.id.tv_carelist_memo);

        k_careVO vo = items.get(i);

        tv_carelist_name.setText(vo.getC_name());
        tv_carelist_phone.setText(vo.getC_phone());
        tv_carelist_address.setText(vo.getC_address());
        tv_carelist_memo.setText(vo.getC_memo());

        return view;
    }
}