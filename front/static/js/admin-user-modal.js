document.addEventListener("DOMContentLoaded", function () {
    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple) {
        new simpleDatatables.DataTable(datatablesSimple);
    }

    // 경고 모달 관련 코드
    var warningModal = document.getElementById("warningModal");
    var closeBtn = document.getElementsByClassName("close")[0];

    document.querySelectorAll('.warning-modal-link').forEach(function (element) {
        element.onclick = function () {
            // 경고 세부 정보 표시
            document.querySelector('.modal-reason').textContent = "기존 경고 사유"; // 이 값을 실제 데이터로 변경해야 합니다.
            document.querySelector('.modal-date').textContent = "경고 생성일"; // 이 값을 실제 데이터로 변경해야 합니다.

            warningModal.style.display = "block";
        }
    });

    closeBtn.onclick = function () {
        warningModal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == warningModal) {
            warningModal.style.display = "none";
        }
    }

    document.getElementById("submitWarning").onclick = function() {
        // 경고 등록 로직 추가
        alert('경고가 등록되었습니다.');
        warningModal.style.display = "none";
    }

    document.getElementById("cancelWarning").onclick = function() {
        warningModal.style.display = "none";
    }
});
