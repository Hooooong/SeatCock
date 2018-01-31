package sku.jvj.seatcock.Model;

/**
 * Created by Android Hong on 2016-06-11.
 */
public class StoreMenu {

    // 메뉴 번호
    private String menuNum;
    // 메뉴 사진
    private String imageResourceId;
    // 메뉴 이름
    private String menuName;
    // 메뉴 가격
    private String menuPrice;

    public String getMenuNum() {
        return menuNum;
    }
    public void setMenuNum(String menuNum) {
        this.menuNum = menuNum;
    }
    public String getImageResourceId() {
        return imageResourceId;
    }
    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = pictureCheck(imageResourceId);
    }
    public String getMenuName() {
        return menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getMenuPrice() {
        return menuPrice;
    }
    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }


    /**
     * 사진유무 체크
     * @param check
     * @return
     */
    private static String pictureCheck(String check){
        String result;
        if(check.equals("0")){
            result = "0";
        }else{
            result =  "http://14.63.213.157/dongimg/menuimg/" + check;
        }
        return result;
    }

}
