package com.company.keepers_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {

    private ArrayList<BoardVO> items = new ArrayList<BoardVO>();

    public void addItem(int b_seq, String b_title, String b_id, String b_signdate) {
        BoardVO vo = new BoardVO(b_seq, b_title, b_id, b_signdate);
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
            view = inflater.inflate(R.layout.board_list, viewGroup, false);
        }

        TextView tv_seq = view.findViewById(R.id.tv_name);
        TextView tv_title = view.findViewById(R.id.tv_phone);
        TextView tv_id = view.findViewById(R.id.tv_address);
        TextView tv_signdate = view.findViewById(R.id.tv_memo);


        BoardVO vo = items.get(i);


        tv_seq.setText(String.valueOf(vo.getB_seq()));
        tv_title.setText(vo.getB_title());
        tv_id.setText(vo.getB_id());
        tv_signdate.setText(vo.getB_signdate());

        return view;
    }
}