package me.developeralfa.calendoer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Decider extends AppCompatActivity {
    SharedPreferences sharedPreferences, taskPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decider);
        sharedPreferences = getSharedPreferences("run", MODE_PRIVATE);
        taskPreferences = getSharedPreferences("tasks", MODE_PRIVATE);

        if (sharedPreferences.contains("notfirst")) {
            Intent pendingIntent = new Intent(this, allTasks.class);
            pendingIntent.putExtra("fName", sharedPreferences.getString("fName", "Guest"));
            pendingIntent.putExtra("lName", sharedPreferences.getString("lName", ""));
            pendingIntent.putExtra("wake", sharedPreferences.getString("wake", "7:00"));
            if (taskPreferences.contains("pending")) {
                pendingIntent.putExtra("pending", toList((HashSet<String>)taskPreferences.getStringSet("pending", null)));
                pendingIntent.putExtra("descpending", toList((HashSet<String>)taskPreferences.getStringSet("descpending", null)));
                pendingIntent.putExtra("done", toList((HashSet<String>)taskPreferences.getStringSet("done", null)));
                pendingIntent.putExtra("descdone", toList((HashSet<String>)taskPreferences.getStringSet("descdone", null)));

            }
            startActivityForResult(pendingIntent, 2);


        } else {
            startActivityForResult(new Intent(this, Welcome_firstrun.class), 1);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("notfirst", true);
                editor.putString("fName", data.getStringExtra("fName"));
                editor.putString("lName", data.getStringExtra("lName"));
                editor.putString("Wake", data.getStringExtra("Wake"));
                editor.commit();
                startActivity(new Intent(Decider.this,Decider.class));
                finish();
            }
        }
        if (requestCode == 2) {
            if (resultCode == 1) {
                SharedPreferences.Editor editor = taskPreferences.edit();
                ArrayList<String> pendingal = data.getStringArrayListExtra("pending");
                ArrayList<String> descpendingal = data.getStringArrayListExtra("descpending");
                ArrayList<String> doneal = data.getStringArrayListExtra("done");
                ArrayList<String> descdoneal = data.getStringArrayListExtra("descdone");
                HashSet<String> pending = toSet(pendingal);
                HashSet<String> descpending = toSet(descpendingal);
                HashSet<String> done = toSet(doneal);
                HashSet<String> descdone = toSet(descdoneal);
                editor.putStringSet("pending", pending);
                editor.putStringSet("descpending", descpending);
                editor.putStringSet("done", done);
                editor.putStringSet("descdone", descdone);
                editor.commit();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private HashSet<String> toSet(ArrayList<String> s) {
        HashSet<String> res = new HashSet<String>();
        for(int i=0;i<s.size();i++) {
            res.add(s.get(i));
        }
        return res;


    }
    private ArrayList<String> toList(HashSet<String> s) {
        ArrayList<String> res = new ArrayList<>();
        for(int i=0;i<s.size();i++) {
            res.add((String)s.toArray()[i]);
        }
        return res;


    }

}
