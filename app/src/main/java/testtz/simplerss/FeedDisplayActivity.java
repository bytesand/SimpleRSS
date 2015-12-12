package testtz.simplerss;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import constructs.RSSItem;

public class FeedDisplayActivity extends AppCompatActivity {
    private static RSSItem value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        //получаем наш фид ввиде класса
        if(extras != null) {
            value = (RSSItem)extras.getSerializable("myfeed");
        }

        if (value == null) NavUtils.navigateUpFromSameTask(this); //идем назад если ничего нет


        //отображаем фиды используя textview
        setContentView(R.layout.activity_feed_display);
        TextView title = (TextView) findViewById(R.id.feedTitle);
        title.setText(value.getTitle());
        TextView feedDate = (TextView) findViewById(R.id.feedDate);
        feedDate.setText(value.getDate());
        TextView feedMainText = (TextView) findViewById(R.id.feedMainText);
        feedMainText.setText(value.getDescription());
    //можно еще и отобразить картинку, но этого не было в тз


    }
}
