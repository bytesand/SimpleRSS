package testtz.simplerss;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import constructs.RSSFeedTitle;
import prefs.Settings;
import tools.CustomAdapterForMainList;

public class MainlistActivity extends AppCompatActivity {
    private  CustomAdapterForMainList adapter_dynamic;
    private  ListView listfeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.mainlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DoINeedaDB();
        initList();
    }

    @Override
    public void onResume(){

        initList();
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addfeed) {
            startAddFeedActivity();
        }
        return super.onOptionsItemSelected(item);
    }



    private void DoINeedaDB(){

        File database=getApplicationContext().getDatabasePath(Settings.myDB);

        if (!database.exists()) {
            //инициализация базы данных
            //создаем таблицу rssDB
            SQLiteDatabase mydb = openOrCreateDatabase(Settings.myDB, MODE_PRIVATE, null);
            //Создаем таблицу под фиды с двумя полями "url" и name
            mydb.execSQL("CREATE TABLE feedslist (id INTEGER PRIMARY KEY AUTOINCREMENT,url varchar,name varchar UNIQUE);"); //name UNIQUE так как мы будем испльзовать его индекс и оренитровать на него foreign key
            mydb.execSQL("INSERT INTO feedslist(url,name)VALUES (\"http://feeds.feedburner.com/AndroidPolice?format=xml\",\"Android Police\"),(\"http://www.getsymphony.com/get-involved/feeds/blog/\",\"PHP Symphony\"),(\"http://500.co/feed/\",\"500 startups\");");
            //добавляем значения по умолчанию
            mydb.execSQL("CREATE TABLE IF NOT EXISTS feeditems (id INTEGER PRIMARY KEY AUTOINCREMENT,item_title varcHAR,item_desc varcHAR,item_date varchAR,item_image varchar,rss_link varchar, rssfeedtitle varchar CONSTRAINT feedtitle REFERENCES feedslist(name) ON DELETE CASCADE ON UPDATE CASCADE);");
            //создаем табличку для айтемов фидам
            mydb.close();

        }

    }




private void initList(){ //иницализация списка

    SQLiteDatabase mydb = openOrCreateDatabase(Settings.myDB, MODE_PRIVATE, null);
    //Выводим список фидов
    final Cursor cur = mydb.rawQuery("SELECT * FROM feedslist;", null);

//    И все эти фиды загружаем в список
    final ArrayList<RSSFeedTitle> RSSlist = new ArrayList<RSSFeedTitle>();
    if (cur != null && cur.moveToFirst()) {
        while (!cur.isAfterLast()) {
            RSSlist.add(new RSSFeedTitle(cur.getString(cur.getColumnIndex("url")),cur.getString(cur.getColumnIndex("name"))));
            cur.moveToNext();
        }
        cur.close();
    }


//инициируем адаптер
    adapter_dynamic = new CustomAdapterForMainList(this, RSSlist);
    adapter_dynamic.notifyDataSetChanged();

    //устанавливаем адаптерr
    listfeed =(ListView) findViewById(R.id.RssFeedsList);
    listfeed.setAdapter(adapter_dynamic);

    //назначаем listener
    listfeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            //получаем выбраный фид со всеми параметрами
            RSSFeedTitle feedvalue = RSSlist.get(arg2);

            //и отправляем на скачку
            Intent ii = new Intent(MainlistActivity.this, DownloadActivity.class);
            ii.putExtra("myfeed", feedvalue);


            startActivity(ii);
            overridePendingTransition(0, 0);

        }
    });


}

    public void startAddFeedActivity(){
        Intent ii = new Intent(MainlistActivity.this, AddfeedActivity.class);
        startActivity(ii);

    }




}
