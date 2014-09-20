package com.loman.david.mp3player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loman.david.data.SqlExcu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 14-8-22.
 */
public class SongsEditActivity extends Activity {

    private int listID;
    private int resultCode;
    private int selecitonCount;
    private SqlExcu sqlExcu;
    private Button positiveButton;
    private ListView listView;
    private boolean[] selection;
    private boolean delete;
    private List<Map<String, String>> list;
    private Map<String, String> map;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs_list);

        Intent intent = getIntent();
        listID = intent.getIntExtra("id", 0);
        delete = intent.getBooleanExtra("delete", false);
        resultCode = 0;

        positiveButton = (Button) findViewById(R.id.yes);
        listView = (ListView) findViewById(R.id.sListView);

        list = new ArrayList<Map<String, String>>();
        map = new HashMap<String, String>();

        sqlExcu = new SqlExcu(SongsEditActivity.this);
        list = sqlExcu.QuerrySQL(sqlExcu.SONGLIST, sqlExcu.LISTID, String.valueOf(listID));
        myAdapter=new MyAdapter(SongsEditActivity.this,list,R.layout.songlist_item);
        listView.setAdapter(myAdapter);

        selection = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            selection[i] = false;
        }
        selecitonCount = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                int defaultColor = Color.argb(0, 255, 255, 255);
//                int selectColor = Color.argb(100, 246, 255, 13);
                if (myAdapter.getSelection(position)) {

                    myAdapter.setSelection(position,false);
                    myAdapter.notifyDataSetChanged();
//                    Log.e("ListView","pos"+position);
//                    selection[position] = false;
//                    View myview = listView.getChildAt(position);
//                    RelativeLayout r = (RelativeLayout) myview.findViewById(R.id.rin);
//                    r.setBackgroundColor(defaultColor);
                    selecitonCount--;

                } else {

                    myAdapter.setSelection(position,true);
                    myAdapter.notifyDataSetChanged();
//                    View myview = listView.getChildAt(position);
//                    RelativeLayout r = (RelativeLayout) myview.findViewById(R.id.rin);
//                    r.setBackgroundColor(selectColor);
//                    Log.e("ListViewClick", "pos" + position);
                    selecitonCount++;

                }

            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selecitonCount > 0) {
                    resultCode = 1;
                }

                Intent intent = new Intent();
                setResult(resultCode, intent);

                finish();
            }
        });

        Intent intent = new Intent();
        this.setResult(resultCode, intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class MyAdapter extends BaseAdapter{

        private List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        private int layout;
        private boolean hasSelect[];
        private Context context;
        private LayoutInflater inflater;
        private int DEFAULTCOLOR =Color.argb(0,255,255,255);
        private int SELECTCOLOR =Color.argb(100,246,255,13);

        public MyAdapter (Context context,List<Map<String,String>> list,int layout){
            super();
            this.context=context;
            this.list=list;
            this.layout=layout;
            this.inflater=LayoutInflater.from(context);
            hasSelect=new boolean [list.size()];
            for (int i=0;i<list.size();i++){
                hasSelect[i]=false;
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView ==null){

                viewHolder=new ViewHolder();
                convertView=inflater.inflate(layout,null);

                viewHolder.title=(TextView)convertView.findViewById(R.id.title);
                viewHolder.time=(TextView)convertView.findViewById(R.id.time);

                convertView.setTag(viewHolder);
            }else{
                viewHolder=(ViewHolder)convertView.getTag();
            }

            viewHolder.title.setText(list.get(position).get(sqlExcu.SONGNAME));
            viewHolder.time.setText(list.get(position).get(sqlExcu.SONGTIME));

            if (hasSelect[position]){
                convertView.setBackgroundColor(SELECTCOLOR);

            }else {
                convertView.setBackgroundColor(DEFAULTCOLOR);
            }

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSelection (int position,boolean selection){
            hasSelect[position]=selection;
        }

        public boolean getSelection (int position){
            return hasSelect[position];
        }
    }

    private class ViewHolder{
        TextView title;
        TextView time;
    }

}
