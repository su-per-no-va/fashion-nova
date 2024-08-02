$(document).ready(function () {

  auth = localStorage.getItem('accessToken');

/*  window.common = {
    getShoppingCart: function () {
      if (auth) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
          jqXHR.setRequestHeader('Authorization', auth);
        });
      console.log(auth)
        $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/carts',
          success: function (response) {
            return response;

          },
          error: function (error) {
            console.error('에러: ' + error.message)
          },
        });
      }
    },

    getWishList: function () {
      if (auth) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
          console.log("auth : " + auth);
          jqXHR.setRequestHeader('Authorization', auth);
        });
        $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/wishlists',
          success: function (response) {
            return response;
          },
          error: function (error) {
            console.error('에러: ' + error.message)
          },
        });
      }
    },
  };*/

  window.common = {
    getShoppingCart: function () {
      // 개발자 도구에서 여기 브레이크 포인트 찍어보실래요?
      if (auth) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
          jqXHR.setRequestHeader('Authorization', auth);
        });
        console.log(auth)

        return $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/carts'
        });
      }
    },

    getWishList: function () {
      if (auth) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
          jqXHR.setRequestHeader('Authorization', auth);
        });
        console.log(auth)

        return $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/wishlists'
        });
      }
    },
  };


  // window.common.cartDto = window.common.getShoppingCart()
  // window.common.wishList = window.common.getWishList()
})
