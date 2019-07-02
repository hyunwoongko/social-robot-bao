package com.welfarerobotics.welfareapplcation.core.contents.tangram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.welfarerobotics.welfareapplcation.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TangramListAdater extends BaseAdapter {

        /* 아이템을 세트로 담기 위한 어레이 */
        private ArrayList<String> mItems = new ArrayList<>();
        private Context context;
        public TangramListAdater(Context context){
        this.context=context;

        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public String getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Context context = parent.getContext();


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.tangram_list, parent, false);
            }


            ImageView rightImage =(ImageView)convertView.findViewById(R.id.image1);
            //ImageView leftImage =(ImageView)convertView.findViewById(R.id.image2);

            /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
            String myItem = getItem(position);


            /* 각 위젯에 세팅된 아이템을 뿌려준다 */

            try {
                Glide.with(context).load(myItem).into(rightImage);
            }catch (Exception e){
                System.out.println("myItem.getRightImage()+++++"+"\n"+e);
            }


            return convertView;
        }

        /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
        public void addItem(String myItem) {

            mItems.add(myItem);

        }
        public void clear() {

            mItems = new ArrayList<>();

         }

    }

