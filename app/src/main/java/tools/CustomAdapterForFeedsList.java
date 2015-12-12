package tools;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import constructs.RSSFeedTitle;
import constructs.RSSItem;
import prefs.Settings;
import testtz.simplerss.R;

public class CustomAdapterForFeedsList extends BaseAdapter {// это адаптер, здесь особо комментировать нечего :)

    private LayoutInflater layoutInflater;
    private ArrayList<RSSItem> RSSlist = new ArrayList<RSSItem>();


    public CustomAdapterForFeedsList(Activity activity, ArrayList<RSSItem> RSSlistinit) {
        RSSlist = RSSlistinit;
        //for urls
     //   mList = list;

        //for names
     //   mList2 = list2;

        //initialize layout inflater
        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return RSSlist.size();
    }


    @Override
    public Object getItem(int position) {
        return RSSlist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View listItem = convertView;

        if (listItem == null) {


            listItem = layoutInflater.inflate(R.layout.dynamic_feed_items, parent, false);
        }


        TextView Title = (TextView) listItem.findViewById(R.id.feed_title);
        TextView date = (TextView) listItem.findViewById(R.id.feed_date);
    //Если развивать в полноценную читалку то эти поля будут нужны
        /*
        TextView desc = (TextView) listItem.findViewById(R.id.feed_desc);

        TextView img = (TextView) listItem.findViewById(R.id.feed_image);
        */
        //dynamically set title and subtitle according to the feed data




        Title.setText(RSSlist.get(position).getTitle());
        date.setText(RSSlist.get(position).getDate());
     /* тоже самое и здесь
        desc.setText(RSSlist.get(position).getDescription());
        date.setText(RSSlist.get(position).getDate());
        img.setText(RSSlist.get(position).getImage());
*/
        return listItem;
    }
}