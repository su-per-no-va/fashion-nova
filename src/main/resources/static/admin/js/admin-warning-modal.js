document.addEventListener('DOMContentLoaded', function () {
    const datatablesSimple = new simpleDatatables.DataTable("#datatablesSimple");

    function addModalEventListeners() {
        const modal = document.getElementById("warningModal");
        const span = document.getElementsByClassName("close")[0];
        const warningLinks = document.querySelectorAll(".modal-link");
        const warningBody = document.querySelector(".warning-body");

        // 모달 여는 함수
        warningLinks.forEach(link => {
            link.addEventListener('click', function () {
                const warnings = [
                    { reason: "말을 싸가지없게 함", date: "2024-08-01" },
                    { reason: "재수가 없음", date: "2024-08-01" }
                ];
                warningBody.innerHTML = warnings.map(warning => `
                            <tr>
                                <td>${warning.reason}</td>
                                <td>${warning.date}</td>
                            </tr>
                        `).join('');
                modal.style.display = "block";
            });
        });

        // 모달 닫는 함수
        span.onclick = function () {
            modal.style.display = "none";
        }

        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }


        // 취소 버튼 클릭 시 모달 닫기
        document.getElementById("cancelWarning").addEventListener('click', function () {
            modal.style.display = "none";
        });
    }

    addModalEventListeners();

    datatablesSimple.on('datatable.draw', function () {
        addModalEventListeners();
    });
});