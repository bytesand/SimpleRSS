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
import prefs.Settings;
import testtz.simplerss.R;

public class CustomAdapterForMainList extends BaseAdapter { // это адаптер, здесь особо комментировать нечего :)

    private LayoutInflater layoutInflater;


    private ArrayList<RSSFeedTitle> RSSlist = new ArrayList<RSSFeedTitle>();



    public CustomAdapterForMainList(Activity activity, ArrayList<RSSFeedTitle> RSSlistinit) {
        RSSlist = RSSlistinit;

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


            listItem = layoutInflater.inflate(R.layout.dynamic_items, parent, false);
        }


        TextView Title = (TextView) listItem.findViewById(R.id.title_dyn);
        TextView url = (TextView) listItem.findViewById(R.id.subtitle_dyn);


        Button deleteBtn = (Button)listItem.findViewById(R.id.delete_btn); //это код для кнопки удаления фида

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                //sql запрос
                SQLiteDatabase mydb=null;
                 mydb = parent.getContext().openOrCreateDatabase(Settings.myDB, parent.getContext().MODE_PRIVATE, null); //открываем базу
                String temps = "DELETE FROM feedslist WHERE  url= \""+ RSSlist.get(position).getUrl() +"\" and name = \""+ RSSlist.get(position).getName() +"\";";

                mydb.execSQL(temps);
                mydb.close();
                //sql запрос
                RSSlist.remove(position); //сначала удаляем значения из базы, а потом из списка
                notifyDataSetChanged();

            }
        });



        url.setText(RSSlist.get(position).getUrl());


        Title.setText(RSSlist.get(position).getName());

        return listItem;
    }
}