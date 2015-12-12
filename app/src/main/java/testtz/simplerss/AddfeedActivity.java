package testtz.simplerss;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import prefs.Settings;

public class AddfeedActivity extends AppCompatActivity { //добавляем новую ленту

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_feed_layout);

        final TextView name = (TextView) findViewById(R.id.name);
        final TextView url = (TextView) findViewById(R.id.url);

        Button addBtn = (Button)findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //sql запрос
                try {


                    SQLiteDatabase mydb = null;
                    mydb = getApplicationContext().openOrCreateDatabase(Settings.myDB, MODE_PRIVATE, null); //открываем базу
                    String temps = "INSERT INTO feedslist(url,name)VALUES (\"" + name.getText() + "\",\"" + url.getText() + "\")"; //пишем фид
                    mydb.execSQL(temps);
                    mydb.close();
                } catch (Exception e)
                {

                }
                finish();
            }
        });
    }

}
