package com.zjy.okhttpnew.lv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.zjy.okhttpnew.R;

import java.util.ArrayList;
import java.util.List;

public class LvActivity extends AppCompatActivity {

    ListView lv;
    StudentAdapter studentAdapter;
    List<Student>list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);
        lv = findViewById(R.id.lv);
        for(int i=0;i<10;i++){
            Student student = new Student();
            student.setId(i);
            student.setName("z"+i);
            student.setAge(10+i);
            list.add(student);
        }
        studentAdapter = new StudentAdapter(LvActivity.this,list);
        lv.setAdapter(studentAdapter);
    }
}