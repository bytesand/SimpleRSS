package testtz.simplerss;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import constructs.RSSItem;
import prefs.Settings;
import tools.CustomAdapterForFeedsList;

public class DisplayFeedsActivity extends AppCompatActivity {
    CustomAdapterForFeedsList adapter_dynamic;
    ListView listfeed;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_feeds);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        //узнаем какие фиды грузить из склюл
        if(extras != null) {
            value = extras.getString("titleFeedName");
        }

        //set the default feed if the value returns null
        if (value == null) NavUtils.navigateUpFromSameTask(this);

        initList();
    }

    private void initList(){  //иницализация списка

        SQLiteDatabase mydb = openOrCreateDatabase(Settings.myDB, MODE_PRIVATE, null);
        //Выводим список фидов
        final Cursor cur = mydb.rawQuery("SELECT * FROM feeditems where rssfeedtitle = \""+value+"\";", null);
        //    И все эти фиды загружаем в список
        final ArrayList<RSSItem> RSSlist = new ArrayList<RSSItem>();

        if (cur != null && cur.moveToFirst()) {

            while (!cur.isAfterLast()) {
                RSSlist.add(new RSSItem(cur.getString(cur.getColumnIndex("item_title")),cur.getString(cur.getColumnIndex("item_desc")),cur.getString(cur.getColumnIndex("item_date")),cur.getString(cur.getColumnIndex("item_image")),cur.getString(cur.getColumnIndex("rss_link"))));
                cur.moveToNext();
            }
            cur.close();
        }


        //инициируем адаптер
        adapter_dynamic = new CustomAdapterForFeedsList(this, RSSlist);
        adapter_dynamic.notifyDataSetChanged();

        //устанавливаем адаптерr
        listfeed =(ListView) findViewById(R.id.listForFeedItems);
        listfeed.setAdapter(adapter_dynamic);

        //назначаем listener
        listfeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //получаем выбраный фид со всеми параметрами
                RSSItem feedvalue = RSSlist.get(arg2);

                //и отображаем в следующей активити
                Intent ii = new Intent(DisplayFeedsActivity.this, FeedDisplayActivity.class);
                ii.putExtra("myfeed", feedvalue);

                startActivity(ii);
                overridePendingTransition(0, 0);

            }
        });


    }




}
