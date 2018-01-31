package sku.jvj.seatcock.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sku.jvj.seatcock.Activity.NoticeActivity;
import sku.jvj.seatcock.Model.Notice;
import sku.jvj.seatcock.R;

/**
 * Created by Android Hong on 2016-10-12.
 */

public class NoticeAdapter extends BaseExpandableListAdapter {

    private ArrayList<Notice> noticeArrayList;
    private LayoutInflater inflater;
    private NoticeViewHolder viewHolder;


    public NoticeAdapter(){
        this.inflater = LayoutInflater.from(NoticeActivity.mContext);
        this.noticeArrayList = new ArrayList<>();
    }

    public void addData(ArrayList<Notice> noticeArrayList){
        this.noticeArrayList = noticeArrayList;
    }

    // 그룹 포지션을 반환한다.
    @Override
    public Notice getGroup(int groupPosition) {
        return noticeArrayList.get(groupPosition);
    }

    // 그룹 사이즈를 반환한다.
    @Override
    public int getGroupCount() {
        return noticeArrayList.size();
    }

    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 그룹뷰 각각의 ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            viewHolder = new NoticeViewHolder();

            view = inflater.inflate(R.layout.noticelist_parent, parent, false);

            viewHolder.noticeListTitle = (TextView) view.findViewById(R.id.noticeListTitle);
            viewHolder.noticeListDate = (TextView) view.findViewById(R.id.noticeListDate);
            viewHolder.noticeListUpdate = (TextView) view.findViewById(R.id.noticeListUpdate);
            viewHolder.noticeListImage = (ImageView) view.findViewById(R.id.noticeListImage);
            view.setTag(viewHolder);
        }else{
            viewHolder = (NoticeViewHolder)view.getTag();
        }

        // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
        if(isExpanded){
            viewHolder.noticeListImage.setImageResource(R.drawable.ic_up);
        }else{
            viewHolder.noticeListImage.setImageResource(R.drawable.ic_down);
        }

        viewHolder.noticeListTitle.setText(getGroup(groupPosition).getNoticeTitle());
        viewHolder.noticeListDate.setText(getGroup(groupPosition).getNoticeDate());

        if(getGroup(groupPosition).getStatus() == 1){
            viewHolder.noticeListUpdate.setText("NEW");
        }else{
            viewHolder.noticeListUpdate.setText("");
        }

        return view;
    }

    // 차일드뷰를 반환한다.
    @Override
    public String getChild(int groupPosition, int childPosition) {
        return noticeArrayList.get(groupPosition).getNoticeContets();
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 차일드뷰 각각의 ROW
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            viewHolder = new NoticeViewHolder();
            view = inflater.inflate(R.layout.noticelist_child, null);
            viewHolder.noticeListContents = (TextView) view.findViewById(R.id.noticeListContents);
            view.setTag(viewHolder);

        }else{
            viewHolder = (NoticeViewHolder)view.getTag();
        }

        viewHolder.noticeListContents.setText(getChild(groupPosition, childPosition));

        return view;
    }

    @Override
    public boolean hasStableIds() { return true; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

    class NoticeViewHolder {
        public TextView noticeListTitle;
        public TextView noticeListDate;
        public TextView noticeListUpdate;
        public ImageView noticeListImage;

        public TextView noticeListContents;
    }
}

