document.addEventListener('DOMContentLoaded', function () {
    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple) {
        new simpleDatatables.DataTable(datatablesSimple);
    }

    var userModal = document.getElementById("userModal");
    var warningModal = document.getElementById("warningModal");

    document.addEventListener('click', function (event) {
        if (event.target.matches('.name-link')) {
            event.preventDefault();
            // 해당 사용자 정보로 모달 업데이트 (데모 데이터를 사용합니다)
            document.querySelector('.modal-id').textContent = 'nado123';
            document.querySelector('.modal-name').textContent = '김나도';
            document.querySelector('.modal-email').textContent = 'nado123@gmail.com';
            document.querySelector('.modal-type').textContent = 'USER';
            document.querySelector('.modal-join').textContent = '2024/08/01';
            document.querySelector('.modal-grade').textContent = 'BRONZE';
            document.querySelector('.modal-total').textContent = '1,700,000';
            document.querySelector('.modal-address').innerHTML = '서울특별시 관악구 정포동 905동 301호<br>경기도 고양시 일산동구 301동 402호';
            document.querySelector('.modal-restriction').innerHTML = '상습 악플로 인한 7일 사용제한 1/3<br>상습 악플로 인한 7일 사용제한 2/3';
            document.querySelector('.modal-recent').innerHTML = '누가 입어도 개쩌는 반팔티 BLACK S 2<br>둘무니 원피스 BLACK S 2';

            userModal.style.display = "block";
        }

        if (event.target.matches('.warning-link')) {
            event.preventDefault();
            // 경고 내역 업데이트 (데모 데이터를 사용합니다)
            var warningBody = document.querySelector('.warning-body');
            warningBody.innerHTML = '<tr><td>상습 악플로 인한 7일 사용제한</td><td>2024-08-01</td></tr><tr><td>재수 없음</td><td>2024-08-01</td></tr>';
            warningModal.style.display = "block";
        }

        if (event.target.matches('.user-close') || event.target.matches('.warning-close') || event.target.matches('#cancelWarning')) {
            userModal.style.display = "none";
            warningModal.style.display = "none";
        }

        if (event.target == userModal) {
            userModal.style.display = "none";
        }
        if (event.target == warningModal) {
            warningModal.style.display = "none";
        }
    });

    window.onclick = function (event) {
        if (event.target == userModal) {
            userModal.style.display = "none";
        }
        if (event.target == warningModal) {
            warningModal.style.display = "none";
        }
    }

});
