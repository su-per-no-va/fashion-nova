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
// font 자신의 host 정보 -> localhost:63345
// backend -> localhost:8080

function onLogin() {
  let username = $('#username').val();
  let password = $('#password').val();

  console.log(host);
  // http://localhost:63342
  // http://localhost:63342/fashion-nova/front/html/signup.html?_ijt=ecr2l9r0cm2mf2vv3enlkck2ku&_ij_reload=RELOAD_ON_SAVE

  $.ajax({
    type: "POST",
    url: `/users/login`,
    contentType: "application/json",
    data: JSON.stringify({userName: username, password: password}),
    xhrFields: {
    	withCredentials: true
    }
  })
  .done(function (res, status, xhr) {
    console.log(res); // 서버 응답 확인
    console.log(xhr);
    //헤더에서 토큰 가져오기
    const token = xhr.getResponseHeader("Authorization");
    const refreshToken = xhr.getResponseHeader('Authorization-Refresh');

    console.log('token: ' + token);
    console.log('refreshToken: ' + refreshToken);

    xhr.setRequestHeader(token,'Authorization');
    xhr.setRequestHeader(refreshToken,'Authorization-Refresh');

    // 토큰을 localStorage에 저장
    localStorage.setItem('accessToken', token);
    localStorage.setItem('refreshToken', refreshToken);

    if (token) {
      // Cookies.set('Authorization', token, {path: '/'});


      $.ajaxPrefilter(function (options, originalOptions, jqXHR) {

        //헤더에 토큰 싣기
        jqXHR.setRequestHeader('Authorization', token);
      });

      alert("로그인 성공!");
      console.log("로그인 성공");
      // window.location.href = host;
    } else {
      alert("로그인 실패!!");
      console.log("로그인 응답만 성공");
    }
  })
  .fail(function (jqXHR, textStatus) {
    alert("로그인 실패");
    console.log("로그인 실패");
    // window.location.href = host + '/users/login'
  });
}

