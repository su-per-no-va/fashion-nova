$(document).ready(function () {

  auth = localStorage.getItem('accessToken');

  window.common = {
    getShoppingCart: function () {
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

    postWishList: function (data) {
      if (auth) {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
          jqXHR.setRequestHeader('Authorization', auth);
        });

        return $.ajax({
          type: 'POST',
          url: '/wishlists',
          contentType:"application/json",
          data: JSON.stringify()
        });
      }
    }
  };

  // window.common.cartDto = window.common.getShoppingCart()
  // window.common.wishList = window.common.getWishList()
})
