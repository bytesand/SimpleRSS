package testtz.simplerss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;

import constructs.DOMParser;
import constructs.RSSFeed;
import constructs.RSSFeedTitle;
import constructs.RSSItem;
import prefs.Settings;

public class DownloadActivity extends Activity {


   private static RSSFeedTitle value;

    RSSFeed rssfeedinstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                //получаем наш фид ввиде класса
                value = (RSSFeedTitle)extras.getSerializable("myfeed");
            }

            if (value == null)  NavUtils.navigateUpFromSameTask(this);
        }

        // Проверяем есть ли у нас интернет
        ConnectivityManager cM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // нету интернета!
        if (cM.getActiveNetworkInfo() == null) {


            startLisActivity(value.getName()); //если нет интернета то переходим в офлайн и используем значения скюл

        } else {

            //открываем лодинг
            setContentView(R.layout.splash);
            new AsyncLoadXMLFeed().execute();

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();

        //получаем наш фид ввиде класса
        if(extras != null) {
            value = (RSSFeedTitle)extras.getSerializable("myfeed");
        }

        //мы не проверяем дейстительно фид ли это или даже правильный ли это урл при добавлении, поэтому если ввели пустое значение, то активити перекинет нас назад
        if (value == null) NavUtils.navigateUpFromSameTask(this);
    }


    private void startLisActivity(String titleFeedName) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("titleFeedName", titleFeedName);
        Intent i = new Intent(DownloadActivity.this, DisplayFeedsActivity.class);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }


    // разбор XML мы будем выполнять как ассинхронную задачу
    private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            DOMParser Do = new DOMParser();
            rssfeedinstance = Do.parseXml(value.getUrl());
            //Пишем айтемы
            SQLiteDatabase mydb = null;
            mydb = openOrCreateDatabase(Settings.myDB, MODE_PRIVATE, null); //открываем базу
        //очищаем таблицу чтобы не дублировать фиды
            mydb.execSQL("DELETE FROM feeditems WHERE rssfeedtitle = \""+ value.getName() +"\"");
            for (RSSItem item : rssfeedinstance._itemlist) {

               SQLiteStatement statement = mydb.compileStatement("INSERT INTO feeditems (item_title, item_desc , item_date, item_image, rss_link, rssfeedtitle) values (?, ?, ?, ?, ?, ?)");
               //это более сложный запрос чтобы не чистить стрингу от кавычек слешов и всего остального будем использовать байдинги
                statement.clearBindings();
                statement.bindString(1,item.getTitle());
                statement.bindString(2,item.getDescription());
                statement.bindString(3,item.getDate());
                statement.bindString(4,item.getImage());
                statement.bindString(5,item.getLink());
                statement.bindString(6,value.getName());
                statement.executeInsert();

            }
                mydb.close();



            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            startLisActivity(value.getName());
        }

    }
}
