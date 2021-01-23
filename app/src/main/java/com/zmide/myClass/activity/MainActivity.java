package com.zmide.myClass.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zmide.R;
import com.zmide.myClass.dao.CourseDao;
import com.zmide.myClass.pojo.Course;
import com.zmide.myClass.view.TimeTableView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CourseDao courseDao = new CourseDao(this);
    private TimeTableView timeTable;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_activity_main);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        timeTable = findViewById(R.id.timeTable);
        timeTable.addListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListener();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取开学时间
        long date = sp.getLong("date", new Date().getTime());
        timeTable.loadData(acquireData(), new Date(date));
        Log.i("test", new Date(date).toString());
    }

    private List<Course> acquireData() {
        List<Course> courses = new ArrayList<>();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("isFirstUse", true)) {//首次使用
            sp.edit().putBoolean("isFirstUse", false).apply();
        }else {
            courses = courseDao.listAll();
        }
        return courses;
    }

    /**
     * 菜单
     */
    public void categoryListener() {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }
}
