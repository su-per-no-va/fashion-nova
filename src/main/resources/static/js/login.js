$(document).ready(function () {
  // 토큰 삭제
  // Cookies.remove('Authorization', {path: '/'});
});

const href = location.href;
const queryString = href.substring(href.indexOf("?") + 1)
if (queryString === 'error') {
  const errorDiv = document.getElementById('login-failed');
  errorDiv.style.display = 'block';
}

const host = 'http://' + window.location.host;

function onLogin() {
  let username = $('#username').val();
  let password = $('#password').val();

  $.ajax({
    type: "POST",
    url: `/users/login`,
    contentType: "application/json",
    data: JSON.stringify({userName: username, password: password}),
  })
  .done(function (res, status, xhr) {
    console.log('Response:', res); // 서버 응답 확인
    //헤더에서 토큰 가져오기
    const token = xhr.getResponseHeader('Authorization');
    const refreshToken = xhr.getResponseHeader('Authorization-Refresh');

    xhr.setRequestHeader(token,'Authorization');
    xhr.setRequestHeader(refreshToken,'Authorization-Refresh');

    // 토큰을 localStorage에 저장
    localStorage.setItem('accessToken', token);
    localStorage.setItem('refreshToken', refreshToken);

    if (token) {
      // Cookies.set('Authorization', token, {path: '/'});


      $.ajaxPrefilter(function (options, originalOptions, jqXHR) {

        //헤더에 토큰 실어보내기
        jqXHR.setRequestHeader('Authorization', token);
      });

      alert("로그인 성공!");
      window.location.href = host;
    } else {
      alert("로그인 실패!!");
    }
  })
  .fail(function (jqXHR, textStatus) {
    alert("로그인 실패");
    window.location.href = host + '/users/login'
  });


}

