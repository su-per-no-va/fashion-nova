$(document).ready(function () {
  // 토큰 삭제
  Cookies.remove('Authorization', {path: '/'});
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
    url: `/api/user/login`,
    contentType: "application/json",
    data: JSON.stringify({username: username, password: password}),
  })
  .done(function (res, status, xhr) {
    const token = xhr.getResponseHeader('Authorization');

    Cookies.set('Authorization', token, {path: '/'})

    $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
      jqXHR.setRequestHeader('Authorization', token);
    });

    window.location.href = host;
  })
  .fail(function (jqXHR, textStatus) {
    alert("Login Fail");
    window.location.href = host + '/api/user/login-page?error'
  });
}