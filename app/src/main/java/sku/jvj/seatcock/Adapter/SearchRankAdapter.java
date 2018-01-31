package sku.jvj.seatcock.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sku.jvj.seatcock.Interface.SearchWord.SearchPresenter;
import sku.jvj.seatcock.Model.SearchWord;
import sku.jvj.seatcock.R;


/**
 * Created by Android Hong on 2016-08-30.
 */
public class SearchRankAdapter extends BaseAdapter {

    private SearchPresenter searchPresenter;
    private Context context;
    private ArrayList<SearchWord> searchRankList;

    public SearchRankAdapter(SearchPresenter searchPresenter){
        this.searchRankList = new ArrayList<>();
        this.searchPresenter = searchPresenter;
    }

    public void addData(ArrayList<SearchWord> searchRankArrayList) {
        this.searchRankList = searchRankArrayList;
    }

    @Override
    public int getCount() {
        return searchRankList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchRankList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        SearchRankHolder holder;
        context = parent.getContext();

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchrank_list, parent, false);
            holder = new SearchRankHolder();
            holder.searchRankNo = (TextView) convertView.findViewById(R.id.searchRankNo);
            holder.searchRank = (TextView) convertView.findViewById(R.id.searchRankTextView);

            convertView.setTag(holder);
        }
        else{
            holder = (SearchRankHolder)convertView.getTag();
        }
        holder.searchRankNo.setText("No. " + searchRankList.get(pos).getNumber());
        holder.searchRank.setText(searchRankList.get(pos).getSearchText());

        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchRankList.get(pos).getSearchText();
                searchPresenter.insertSearchWord(searchText);
                searchPresenter.navigateToSearchResult(searchText);
            }
        });

        return convertView;
    }


    public class SearchRankHolder{
        public TextView searchRankNo = null;
        public TextView searchRank = null;
    }
}
