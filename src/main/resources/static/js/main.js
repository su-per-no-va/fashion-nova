
(function ($) {
    "use strict";

    /*[ Load page ]
    ===========================================================*/
    $(".animsition").animsition({
        inClass: 'fade-in',
        outClass: 'fade-out',
        inDuration: 1500,
        outDuration: 800,
        linkElement: '.animsition-link',
        loading: true,
        loadingParentElement: 'html',
        loadingClass: 'animsition-loading-1',
        loadingInner: '<div class="loader05"></div>',
        timeout: false,
        timeoutCountdown: 5000,
        onLoadEvent: true,
        browser: [ 'animation-duration', '-webkit-animation-duration'],
        overlay : false,
        overlayClass : 'animsition-overlay-slide',
        overlayParentElement : 'html',
        transition: function(url){ window.location.href = url; }
    });
    
    /*[ Back to top ]
    ===========================================================*/
    var windowH = $(window).height()/2;

    $(window).on('scroll',function(){
        if ($(this).scrollTop() > windowH) {
            $("#myBtn").css('display','flex');
        } else {
            $("#myBtn").css('display','none');
        }
    });

    $('#myBtn').on("click", function(){
        $('html, body').animate({scrollTop: 0}, 300);
    });


    /*==================================================================
    [ Fixed Header ]*/
    var headerDesktop = $('.container-menu-desktop');
    var wrapMenu = $('.wrap-menu-desktop');

    if($('.top-bar').length > 0) {
        var posWrapHeader = $('.top-bar').height();
    }
    else {
        var posWrapHeader = 0;
    }
    

    if($(window).scrollTop() > posWrapHeader) {
        $(headerDesktop).addClass('fix-menu-desktop');
        $(wrapMenu).css('top',0); 
    }  
    else {
        $(headerDesktop).removeClass('fix-menu-desktop');
        $(wrapMenu).css('top',posWrapHeader - $(this).scrollTop()); 
    }

    $(window).on('scroll',function(){
        if($(this).scrollTop() > posWrapHeader) {
            $(headerDesktop).addClass('fix-menu-desktop');
            $(wrapMenu).css('top',0); 
        }  
        else {
            $(headerDesktop).removeClass('fix-menu-desktop');
            $(wrapMenu).css('top',posWrapHeader - $(this).scrollTop()); 
        } 
    });


    /*==================================================================
    [ Menu mobile ]*/
    $('.btn-show-menu-mobile').on('click', function(){
        $(this).toggleClass('is-active');
        $('.menu-mobile').slideToggle();
    });

    var arrowMainMenu = $('.arrow-main-menu-m');

    for(var i=0; i<arrowMainMenu.length; i++){
        $(arrowMainMenu[i]).on('click', function(){
            $(this).parent().find('.sub-menu-m').slideToggle();
            $(this).toggleClass('turn-arrow-main-menu-m');
        })
    }

    $(window).resize(function(){
        if($(window).width() >= 992){
            if($('.menu-mobile').css('display') == 'block') {
                $('.menu-mobile').css('display','none');
                $('.btn-show-menu-mobile').toggleClass('is-active');
            }

            $('.sub-menu-m').each(function(){
                if($(this).css('display') == 'block') { console.log('hello');
                    $(this).css('display','none');
                    $(arrowMainMenu).removeClass('turn-arrow-main-menu-m');
                }
            });
                
        }
    });


    /*==================================================================
    [ Show / hide modal search ]*/
    $('.js-show-modal-search').on('click', function(){
        $('.modal-search-header').addClass('show-modal-search');
        $(this).css('opacity','0');
    });

    $('.js-hide-modal-search').on('click', function(){
        $('.modal-search-header').removeClass('show-modal-search');
        $('.js-show-modal-search').css('opacity','1');
    });

    $('.container-search-header').on('click', function(e){
        e.stopPropagation();
    });


    /*==================================================================
    [ Isotope ]*/
    var $topeContainer = $('.isotope-grid');
    var $filter = $('.filter-tope-group');

    // filter items on button click
    $filter.each(function () {
        $filter.on('click',  'button, div, a', function () {
            var filterValue = $(this).attr('data-filter');
            $topeContainer.isotope({filter: filterValue});
            loadWishlist();
        });
        
    });
    //상품 클릭이벤트 추가

    document.getElementById("ALL").addEventListener("click", function () {
        let sort = $(".filter-link-active").data("sort");
        console.log(sort);
        sendRequest("", sort, 1);
    });

    document.getElementById("TOP").addEventListener("click", function () {
        let sort = $(".filter-link-active").data("sort");
        console.log(sort);
        sendRequest("TOP", sort,1);
    });

    document.getElementById("BOTTOM").addEventListener("click", function () {
        let sort = $(".filter-link-active").data("sort");
        console.log(sort);
        sendRequest("BOTTOM", sort, 1);
    });

    document.getElementById("SHOES").addEventListener("click", function () {
        let sort = $(".filter-link-active").data("sort");
        console.log(sort);
        sendRequest("SHOES", sort, 1);
    });

    document.getElementById("GOODS").addEventListener("click", function () {
        let sort = $(".filter-link-active").data("sort");
        console.log(sort);
        sendRequest("GOODS", sort, 1);
    });

    $(document).ready(function () {
        var currentCategory = "";
        var currentSort = "";

        // 로컬 스토리지에서 선택된 카테고리 값을 가져옴
        var selectedCategory = localStorage.getItem('selectedCategory');
        if (selectedCategory) {
            currentCategory = selectedCategory; // 현재 카테고리에 선택된 카테고리 설정
            localStorage.removeItem('selectedCategory'); // 사용 후 로컬 스토리지 초기화
        }

        // Isotope 초기화
        var $topeContainer = $('.isotope-grid').isotope({
            itemSelector: '.isotope-item',
            layoutMode: 'fitRows',
        });

        // 초기 로드 시, 선택된 카테고리가 있다면 해당 카테고리로 필터링
        if (currentCategory) {
            $topeContainer.isotope({ filter: '.' + currentCategory.toLowerCase() });

            // 필터 버튼의 상태를 업데이트
            $('.filter-tope-group button').removeClass('how-active1');
            $('.filter-tope-group button[data-filter="' + currentCategory + '"]').addClass('how-active1');
        } else {
            $topeContainer.isotope({ filter: '*' });
        }

        // 카테고리 버튼 클릭 이벤트
        $(".category-btn").on("click", function () {
            currentCategory = $(this).data("filter"); // 클릭한 버튼의 데이터 필터 값 가져오기

            // 모든 카테고리 버튼에서 active 클래스 제거
            $(".category-btn").removeClass("category-active");

            // 현재 클릭된 버튼에 active 클래스 추가
            $(this).addClass("category-active");

            // Isotope 필터링 적용
            $topeContainer.isotope({ filter: '.' + currentCategory.toLowerCase() });
        });

        // 정렬 버튼 클릭 이벤트
        $('.filter-link').on('click', function (e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            // 모든 정렬 링크에서 filter-link-active 클래스 제거
            $('.filter-link').removeClass('filter-link-active');

            // 현재 클릭된 링크에 filter-link-active 클래스 추가
            $(this).addClass('filter-link-active');

            currentSort = $(this).data('sort'); // 클릭된 버튼의 data-sort 속성 값 가져오기
            sendRequest(currentCategory, currentSort, 1); // 현재 카테고리와 정렬 기준을 사용하여 요청
        });
    });

    function sendRequest(category, sort, page) {
        var xhr = new XMLHttpRequest();
        var url = `/products/product?sort=${sort}&category=${category}&page=${page}`; // 쿼리 파라미터로 URL 생성

        xhr.open("GET", url, true); // GET 요청 초기화
        xhr.setRequestHeader("Content-Type", "application/json");

        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {
                var response = JSON.parse(xhr.responseText); // 서버 응답 처리
                console.log(response); // 서버 응답 출력
                updateProductList(response.content);
                // TODO: 응답 데이터를 사용해 UI 업데이트
            }
        };

        xhr.onerror = function () {
            console.error("요청 중 오류 발생"); // 오류 처리
        };

        xhr.send(); // 요청 전송
    }
    function updateProductList(products) {
        var container = document.getElementById('productListArea');
        container.innerHTML = ""; // 기존 내용 삭제

        products.forEach(function (product) {
            var productCard = `
            <div class="col-sm-6 col-md-4 col-lg-3 p-b-35 isotope-item ${product.productStatus.toLowerCase()}">
                <div class="block2">
                    <div class="block2-pic hov-img0">
                        <img src="${product.imageUrl}" alt="IMG-PRODUCT">
                    </div>
                    <div class="block2-txt flex-w flex-t p-t-14">
                        <div class="block2-txt-child1 flex-col-l">
                            <a href="product-detail.html?id=${product.id}" class="stext-104 cl4 hov-cl1 trans-04 js-name-b2 p-b-6">
                                ${product.product}
                            </a>
                            <span class="stext-105 cl3">
                                ${product.price.toLocaleString()}원
                            </span>
                        </div>
                        <div class="block2-txt-child2 flex-r p-t-3">
                            <a href="#" class="btn-addwish-b2 dis-block pos-relative js-addwish-b2">
                                <img class="icon-heart1 dis-block trans-04" src="../images/icons/icon-heart-01.png" alt="ICON">
                                <img class="icon-heart2 dis-block trans-04 ab-t-l" src="../images/icons/icon-heart-02.png" alt="ICON">
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        `;

            container.innerHTML += productCard; // 생성한 HTML을 컨테이너에 추가
        });
    }


    // init Isotope
    $(window).on('load', function () {
        var $grid = $topeContainer.each(function () {
            $(this).isotope({
                itemSelector: '.isotope-item',
                layoutMode: 'fitRows',
                percentPosition: true,
                animationEngine : 'best-available',
                masonry: {
                    columnWidth: '.isotope-item'
                }
            });
        });
    });

    var isotopeButton = $('.filter-tope-group button');

    $(isotopeButton).each(function(){
        $(this).on('click', function(){
            for(var i=0; i<isotopeButton.length; i++) {
                $(isotopeButton[i]).removeClass('how-active1');
            }

            $(this).addClass('how-active1');
        });
    });

    /*==================================================================
    [ Filter / Search product ]*/
    $('.js-show-filter').on('click',function(){
        $(this).toggleClass('show-filter');
        $('.panel-filter').slideToggle(400);

        if($('.js-show-search').hasClass('show-search')) {
            $('.js-show-search').removeClass('show-search');
            $('.panel-search').slideUp(400);
        }
    });

    $('.js-show-search').on('click',function(event){
        $(this).toggleClass('show-search');
        $('.panel-search').slideToggle(200);

        if($('.js-show-filter').hasClass('show-filter')) {
            $('.js-show-filter').removeClass('show-filter');
            $('.panel-filter').slideUp(400);
        }
    });

    /*==================================================================
    [ Cart ]*/
    $('.js-show-cart').on('click',function(){
        $('.js-panel-cart').addClass('show-header-cart');
    });

    $('.js-hide-cart').on('click',function(){
        $('.js-panel-cart').removeClass('show-header-cart');
    });

    /*==================================================================
    [ Cart ]*/
    $('.js-show-sidebar').on('click',function(){
        $('.js-sidebar').addClass('show-sidebar');
    });

    $('.js-hide-sidebar').on('click',function(){
        $('.js-sidebar').removeClass('show-sidebar');
    });

    /*==================================================================
    [ +/- num product ]*/
    $('.btn-num-product-down').on('click', function(){
        var numProduct = Number($(this).next().val());
        if(numProduct > 0) $(this).next().val(numProduct - 1);
    });

    $('.btn-num-product-up').on('click', function(){
        var numProduct = Number($(this).prev().val());
        $(this).prev().val(numProduct + 1);
    });

    /*==================================================================
    [ Rating ]*/
    $('.wrap-rating').each(function(){
        var item = $(this).find('.item-rating');
        var rated = -1;
        var input = $(this).find('input');
        $(input).val(0);

        $(item).on('mouseenter', function(){
            var index = item.index(this);
            var i = 0;
            for(i=0; i<=index; i++) {
                $(item[i]).removeClass('zmdi-star-outline');
                $(item[i]).addClass('zmdi-star');
            }

            for(var j=i; j<item.length; j++) {
                $(item[j]).addClass('zmdi-star-outline');
                $(item[j]).removeClass('zmdi-star');
            }
        });

        $(item).on('click', function(){
            var index = item.index(this);
            rated = index;
            $(input).val(index+1);
        });

        $(this).on('mouseleave', function(){
            var i = 0;
            for(i=0; i<=rated; i++) {
                $(item[i]).removeClass('zmdi-star-outline');
                $(item[i]).addClass('zmdi-star');
            }

            for(var j=i; j<item.length; j++) {
                $(item[j]).addClass('zmdi-star-outline');
                $(item[j]).removeClass('zmdi-star');
            }
        });
    });

    /*==================================================================
    [ Show modal1 ]*/
    $('.js-show-modal1').on('click',function(e){
        e.preventDefault();
        $('.js-modal1').addClass('show-modal1');
    });

    $('.js-hide-modal1').on('click',function(){
        $('.js-modal1').removeClass('show-modal1');
    });



})(jQuery);

