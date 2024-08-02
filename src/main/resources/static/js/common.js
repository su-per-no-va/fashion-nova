$(document).ready(function () {

  auth = localStorage.getItem('accessToken');


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
          url: '/carts'
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
          url: '/wishlists'
        });
      }
    },
  };


  // window.common.cartDto = window.common.getShoppingCart()
  // window.common.wishList = window.common.getWishList()
})
