package com.example.administrator.timeRecording;

import android.os.Bundle;
import android.app.Activity;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.administrator.timeRecording.BaseAcivity.BaseActivity;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.bCallBack;

import static android.widget.Toast.*;

public class MapShowActivity extends BaseActivity {
    
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);
        
        searchView = (SearchView) findViewById(R.id.search_view);
        
        searchView.setOnClickSearch(new ICallBack(){
            @Override
            public void SearchAciton(String string) {
                makeText(MapShowActivity.this,"我收到了："+string, LENGTH_SHORT).show();
            }
        });
        
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
    }

}
