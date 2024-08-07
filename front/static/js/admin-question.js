// 모달 관련 스크립트
document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("myModal");
    var modalText = document.getElementById("modal-text");
    var span = document.getElementsByClassName("close")[0];

    document.querySelectorAll('.modal-link').forEach(function (element) {
        element.onclick = function () {
            modalText.innerHTML = this.getAttribute('data-modal-content');
            modal.style.display = "block";
        }
    });

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
});