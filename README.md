# 실시간 자리 확인 및 예약 시스템 ( 부제 : 자리콕 )
~~현재 서버 중단으로 App 실행 불가~~</br>

## 기간

2016.06.12 ~ 2016.11.14

## 역할

팀장, DB 설계, Android 전체 담당

## 소개

‘자리콕’은 점포의 자리 정보를 언제, 어디서든 사용자가 원하는 기능에 따라 정보를 제공하는 소프트웨어 시스템입니다.</br>
‘자리콕’을 사용하는 다양한 분야의 점포를 검색하여 지도로 확인할 수 있고, 좌석 정보 뿐 아니라 점포의 정보, 방문자의 리뷰 등 다양한 컨텐츠를 제공합니다. 또한 사용자는 원하는 점포를 예약할 수 있고 대기시간을 활용 할 수 있도록 대기번호를 모바일로 발급하는 기능을 제공합니다.

## 개발 환경

개발 언어 : Java, PHP</br>
개발 환경 : JDK 1.8, SDK(Min 21, Target 23), Apache 2.2, PHP 5.3</br>
데이터베이스 : MySQL 5.5</br>
개발 도구 : Eclipse Mars, Android Studio 2.1.2, SQLyog

## 화면

![screenshot1](https://github.com/Hooooong/SeatCock/blob/master/image/screen1.jpg)
![screenshot2](https://github.com/Hooooong/SeatCock/blob/master/image/screen2.jpg)
![screenshot3](https://github.com/Hooooong/SeatCock/blob/master/image/screen3.jpg)
![screenshot4](https://github.com/Hooooong/SeatCock/blob/master/image/screen4.jpg)
![screenshot5](https://github.com/Hooooong/SeatCock/blob/master/image/screen5.jpg)

## 사용 Skills

1. Navigation Page 구현 [[소스코드]](https://github.com/Hooooong/SeatCock/blob/master/app/src/main/java/sku/jvj/seatcock/Activity/MainActivity.java)

  1. `DrawerLayout`, `NavigationView` 를 이용하여 구현

    - 공지가 발생했을 경우에 `NavigationView` 를 업데이트

    ```java
    toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        // DrawerLayout 의 상태가 변경되면 호출
        // 예를 들어 나오거나, 들어갔을 때 변경되게 끔 호출
        @Override
        public void onDrawerStateChanged(int newState) {

            // 공지 관련 데이터 Load
            int noticeNum = mainPresenter.getNotiCount();

            // 공지가 변경된 것이 있으면 숫자 표시
            if (noticeNum > 0) {
                noticeBellTextView.setVisibility(View.VISIBLE);
                noticeBellTextView.setText(Integer.toString(noticeNum));
            } else {
                noticeBellTextView.setVisibility(View.GONE);
            }
        }
    };
    ```

    - 로그인 한 User 가 있을 경우에도 `NavigationView` 를 업데이트

    ```java
    // 로그인 했을 때
    @Override
    public void isLogin(final KakaoUser kakaoUser) {
        if (kakaoUser != null) {
            // 로그인 한 경우
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    noLoginLayout.setVisibility(View.GONE);
                    LoginLayout.setVisibility(View.VISIBLE);
                    userName.setText(kakaoUser.getName());
                    userPhone.setText(kakaoUser.getPhoneNumber());

                    if ("noimage".equals(kakaoUser.getProfile_image())) {
                        userImage.setImageResource(R.drawable.user_icon);
                    } else {
                        Glide.with(getApplicationContext())
                                .load(kakaoUser.getProfile_image())
                                .into(userImage);
                    }
                    navigationView.getMenu().findItem(R.id.nav_user).setTitle("로그아웃");
                }
            });
        } else {
            // 로그인 안한 경우
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    noLoginLayout.setVisibility(View.VISIBLE);
                    LoginLayout.setVisibility(View.GONE);
                    navigationView.getMenu().findItem(R.id.nav_user).setTitle("로그인");
                }
            });
        }
    }
    ```

2. 외부 로그인 API( Kakao )

  - SDK 설정 : [kakao SDK 설정](https://developers.kakao.com/docs/android/user-management)

  1. Session 을 통해 회원 유무 체크

    - `onCreate()` 호출 시 Session, SessionCallback 설정

    - `onActivityResult()` 에서 SessionCallback 처리, `onDestroy()` 에서 SessionCallback 삭제

    ```JAVA
    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 생략

        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    // 로그인 activity를 이용하여 sdk에서 필요로 하는 activity를 띄우기 때문에
    // 해당 결과를 세션에도 넘겨줘서 처리할 수 있도록 Session#handleActivityResult를 호출해 줍니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 세션의 상태가 변경될 때 불리는 세션 콜백을 삭제합니다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }
    ```

    - Session 검사

    ```java
    private class SessionCallback implements ISessionCallback {
        // access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태.
        // 일반적으로 로그인 후의 다음 activity로 이동한다.
        @Override
        public void onSessionOpened() {
            requestSave();
        }

        // memory와 cache에 session 정보가 전혀 없는 상태.
        // 일반적으로 로그인 버튼이 보이고 사용자가 클릭시 동의를 받아 access token 요청을 시도한다
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            } else {
                Logger.e(exception);
                setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
            }                                            // 로그인화면을 다시 불러옴
        }
    }

    /**x
     * 사용자의 상태를 알아보고 저장하기 위해 me API 호출을 한다.
     */
    protected void requestSave() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onNotSignedUp() {
                redirectSignupActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                redirectMainActivity();
            }
        });
    }

    /**
     * 세션 연결 시
     * 회원이 아닐 경우 회원가입 화면으로 넘어가기 위한 메소드
     */
    protected void redirectSignupActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * 세션 연결 시
     * 회원일 경우 정보 저장 후 메인화면으로 넘어가기 위한 메소드
     */
    protected void redirectMainActivity() {
        Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    ```

  2. 회원이 아닐 경우에는 서버에 회원가입 요청

    -  Kakao 서버에 회원 가입 요청

    ```java
    // 회원 가입 요청
    // Kakao 서버에 저장이 된다.
    UserManagement.requestSignup(new ApiResponseCallback<Long>() {
        @Override
        public void onSessionClosed(ErrorResult errorResult) {
            signUpPresenter.failOfTimeOut();
        }

        @Override
        public void onNotSignedUp() {
            signUpPresenter.failOfTimeOut();
        }

        @Override
        public void onSuccess(Long result) {
            String userid = Long.toString(result);
            // Database 에 저장
            setProfilePath(userid,name,jubun,phoneNumber);
        }
    }, properties);
    ```

    - 실 서버에 회원 정보 저장

    ```java
    // 회원이 있는 지 요청
    // kakao 서버에 회원이 저장이 잘 되어있으면 Database 저장
    UserManagement.requestMe(new MeResponseCallback() {
        @Override
        public void onSessionClosed(ErrorResult errorResult) {

        }

        @Override
        public void onNotSignedUp() {

        }

        @Override
        public void onSuccess(UserProfile userProfile) {
            String userProfilePath = null;
            if (userProfile.getProfileImagePath().isEmpty() || userProfile.getProfileImagePath() == null) {
                // 이미지 존재하지 않음.
                 userProfilePath = "noimage";
            } else {
                // 이미지 존재
                userProfilePath = userProfile.getProfileImagePath();
            }
            // Database에 저장
            userData.insertToUser(userid,name,jubun,phoneNumber,userProfilePath);
        }
    });
    ```

3. HTTP 통신

4. Daum Map

5. FCM

6. Google Place

7. MVP Architecture 사용

## 소스 코드

  - [전체 소스코드](https://github.com/Hooooong/SeatCock/tree/master/app/src/main)
