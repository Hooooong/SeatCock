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
 * SearchWordAdapter 생성
 */
public class SearchWordAdapter extends BaseAdapter {

    private  SearchPresenter searchPresenter;
    private Context context;
    private ArrayList<SearchWord> arrayList;

    /**
     * 생성자
     */
    public SearchWordAdapter(SearchPresenter searchPresenter) {
        this.arrayList = new ArrayList<>();
        this.searchPresenter = searchPresenter;
    }
    public void addData(ArrayList<SearchWord> searchWordArrayList){
        this.arrayList = searchWordArrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 출력될 아이템 관리
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        context = parent.getContext();
        SearchWordHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchword_list, parent, false);
            holder = new SearchWordHolder();
            holder.searchWord = (TextView) convertView.findViewById(R.id.searchWordTextView);
            holder.searchDate = (TextView) convertView.findViewById(R.id.searchWordDateTextView);
            holder.searchDelete = (TextView) convertView.findViewById(R.id.searchWordDeleteTextView);
            holder.searchWord.setTag(pos);
            holder.searchDelete.setTag(pos);

            convertView.setTag(holder);
        } else {
            holder = (SearchWordHolder) convertView.getTag();
        }

        holder.searchWord.setText(arrayList.get(position).getSearchText());
        holder.searchDate.setText(arrayList.get(position).getDate());

        // 버튼을 터치 했을 때 이벤트 발생
        holder.searchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = arrayList.get(pos).getSearchText();
                searchPresenter.showDialog(searchText);
            }
        });


        // 리스트 아이템을 터치 했을 때 이벤트 발생
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = arrayList.get(pos).getSearchText();
                searchPresenter.insertSearchWord(searchText);
                searchPresenter.navigateToSearchResult(searchText);
            }
        });

        return convertView;
    }
   public class SearchWordHolder {
        public TextView searchWord;
        public TextView searchDate;
        public TextView searchDelete;
    }
}
