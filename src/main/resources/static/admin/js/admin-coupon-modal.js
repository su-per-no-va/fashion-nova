document.addEventListener("DOMContentLoaded", function () {
    var couponModal = document.getElementById("myModal");
    var mileageModal = document.getElementById("mileageModal");
    var spanCoupon = document.getElementsByClassName("close")[0];
    var spanMileage = document.getElementsByClassName("close-mileage")[0];

    // 쿠폰 모달 링크에 이벤트 리스너 바인딩 함수
    function bindCouponModalLinks() {
        document.querySelectorAll('.modal-link').forEach(function (element) {
            element.onclick = function () {
                couponModal.style.display = "block";
            }
        });
    }

    // 마일리지 모달 링크에 이벤트 리스너 바인딩 함수
    function bindMileageModalLinks() {
        document.querySelectorAll('.mileage-modal-link').forEach(function (element) {
            element.onclick = function () {
                mileageModal.style.display = "block";
            }
        });
    }

    // 쿠폰 모달 닫기 버튼 이벤트 리스너
    spanCoupon.onclick = function () {
        couponModal.style.display = "none";
    }

    // 마일리지 모달 닫기 버튼 이벤트 리스너
    spanMileage.onclick = function () {
        mileageModal.style.display = "none";
    }

    // 모달 외부 클릭 시 모달 닫기
    window.onclick = function (event) {
        if (event.target == couponModal) {
            couponModal.style.display = "none";
        }
        if (event.target == mileageModal) {
            mileageModal.style.display = "none";
        }
    }

    document.getElementById("cancelReply").onclick = function () {
        couponModal.style.display = "none";
    }

    document.getElementById("cancelMileage").onclick = function () {
        mileageModal.style.display = "none";
    }

    // 등록 버튼 클릭 시 알럿 표시
    document.getElementById("submitReply").onclick = function () {
        alert('쿠폰이 지급되었습니다.');
        couponModal.style.display = "none";
    }

    // 마일리지 지급 버튼 클릭 시 알럿 표시
    document.getElementById("submitMileage").onclick = function () {
        alert('마일리지가 지급되었습니다.');
        mileageModal.style.display = "none";
    }

    // DataTable 초기화 및 이벤트 리스너 바인딩
    const datatablesSimple = document.getElementById('datatablesSimple');
    if (datatablesSimple) {
        var dataTable = new simpleDatatables.DataTable(datatablesSimple);

        // DataTable 이벤트에 바인딩
        dataTable.on('datatable.page', function () {
            bindCouponModalLinks();
            bindMileageModalLinks();
        });
        dataTable.on('datatable.sort', function () {
            bindCouponModalLinks();
            bindMileageModalLinks();
        });
        dataTable.on('datatable.search', function () {
            bindCouponModalLinks();
            bindMileageModalLinks();
        });
    }

    // 페이지가 처음 로드될 때 모달 링크에 이벤트 리스너 바인딩
    bindCouponModalLinks();
    bindMileageModalLinks();
});
