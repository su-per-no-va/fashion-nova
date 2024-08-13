$(document).ready(function () {

  auth = localStorage.getItem('accessToken');

  if (auth) {
    $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
      jqXHR.setRequestHeader('Authorization', auth);
    });

    $.ajax({
      type: 'GET',
      url: '/mileages',
      success: function (response) {
        console.log("사용자 로그인 확인");
        // 로그인 상태에서 헤더 변경
        $('#auth-btn').attr("href", "#").text("Logout");

        // 로그아웃 버튼 클릭 시
        $('#auth-btn').on('click', function (e) {
          e.preventDefault();
          localStorage.removeItem('accessToken'); // 토큰 삭제
          window.location.href ='/index.html'; // 페이지 리로드
        });
      },
      error: function (error) {
        alert('로그인 만료, 다시 로그인 해 주세요.');
        window.location.href = 'login.html'
      }
    });

    window.common = {
      getShoppingCart: function () {
        return $.ajax({
          type: 'GET',
          url: '/carts'
        });
      },

      getWishList: function () {
        return $.ajax({
          type: 'GET',
          url: '/wishlists'
        });
      },
    }
  }
})
