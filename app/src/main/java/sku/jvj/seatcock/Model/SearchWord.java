package sku.jvj.seatcock.Model;

/**
 * Created by Android Hong on 2016-08-29.
 */
public class SearchWord {

    int _id;

    String number;
    String searchText;
    String date;

    public SearchWord(String number, String searchText) {
        this.number = number;
        this.searchText = searchText;
    }

    public SearchWord(int _id, String searchText, String date) {
        this._id = _id;
        this.searchText = searchText;
        this.date = date;
    }

    public String getSearchText() {
        return searchText;
    }
    public String getDate() {
        return date;
    }
    public String getNumber() {
        return number;
    }

}
