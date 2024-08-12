document.addEventListener("DOMContentLoaded", function () {
    var modal = document.getElementById("myModal");
    var span = document.getElementsByClassName("close")[0];

    // 모달 링크에 이벤트 리스너 바인딩 함수
    function bindModalLinks() {
        document.querySelectorAll('.modal-link').forEach(function (element) {
            element.onclick = function () {
                var title = this.textContent;
                var category = this.getAttribute('data-category');
                var author = this.getAttribute('data-author');
                var content = this.getAttribute('data-content');
                var attachment = this.getAttribute('data-attachment') || '첨부된 이미지가 없습니다.';

                document.querySelector(".modal-title").textContent = title;
                document.querySelector(".modal-category").textContent = category;
                document.querySelector(".modal-author").textContent = author;
                document.querySelector(".modal-text").textContent = content;
                document.querySelector(".modal-attachment").textContent = attachment;

                modal.style.display = "block";
            }
        });
    }

    // 모달 닫기 버튼 이벤트 리스너
    span.onclick = function () {
        modal.style.display = "none";
    }

    // 모달 외부 클릭 시 모달 닫기
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    document.getElementById("cancelReply").onclick = function () {
        modal.style.display = "none";
    }

    // 등록 버튼 클릭 시 알럿 표시
    document.getElementById("submitReply").onclick = function () {
        alert('답변이 등록되었습니다.');
        modal.style.display = "none";
    }

    // DataTable 초기화 및 이벤트 리스너 바인딩
    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple) {
        var dataTable = new simpleDatatables.DataTable(datatablesSimple);

        // DataTable 이벤트에 바인딩
        dataTable.on('datatable.page', bindModalLinks);
        dataTable.on('datatable.sort', bindModalLinks);
        dataTable.on('datatable.search', bindModalLinks);
    }

    // 페이지가 처음 로드될 때 모달 링크에 이벤트 리스너 바인딩
    bindModalLinks();
});
