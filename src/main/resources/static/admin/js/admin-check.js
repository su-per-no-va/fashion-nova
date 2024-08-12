$(document).ready(function () {
  const auth = localStorage.getItem('accessToken');

  if (auth === null || auth === "null") {
    alert("어드민 로그인 후 이용해 주세요");
    window.location.href = 'admin-login.html';
    return; // 로그인하지 않은 상태에서 아래 코드가 실행되지 않도록 종료
  }

  if (auth) {
    $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
      jqXHR.setRequestHeader('Authorization', auth);
    });
  }

  $.ajax({
    type: 'GET',
    url: '/users/role',
    success: function (response) {
      if (response.userRole !== "ADMIN") {
        alert('운영자페이지 입니다, 운영자계정으로 로그인 해 주세요.');
        window.location.href = 'admin-login.html';
      }
    },
    error: function (error) {
      alert('운영자페이지 입니다, 운영자계정으로 로그인 해 주세요.');
      window.location.href = 'admin-login.html';
    }
  });
});