package sku.jvj.seatcock.Model;

/**
 * Created by Android Hong on 2016-08-09.
 */
public class KakaoUser {

    //고유 식별 번호
    private String id;
    //닉네임
    private String nickname;
    //이름
    private String name;
    //프로필사진
    private String profile_image;
    // 휴대폰 번호
    private String PhoneNumber;
    // 주민번호(7자리)
    private int jubun;
    //토큰
    private String token;

    private static KakaoUser instance;

    private KakaoUser() {}

    public static KakaoUser getInstance() {
        if ( instance == null )
            instance = new KakaoUser();
        return instance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getJubun() {
        return jubun;
    }

    public void setJubun(int jubun) {
        this.jubun = jubun;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "KakaoUser{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", jubun=" + jubun +
                ", token='" + token + '\'' +
                '}';
    }
}
