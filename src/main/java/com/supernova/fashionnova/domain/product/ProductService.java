package com.supernova.fashionnova.domain.product;

import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.order.OrderDetail;
import com.supernova.fashionnova.domain.order.OrderDetailRepository;
import com.supernova.fashionnova.domain.product.dto.ProductResponseDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.payment.PayAction;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productDetailRepository;

    /**
     * 조건별 상품 검색
     *
     * @param sorted
     * @param category
     * @param size
     * @param color
     * @param search
     * @param page
     * @return 페이징
     */
    public Page<ProductResponseDto> getProductList(String sorted, String category, String size,
        String color, String search, int page) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sorted);
        Pageable pageable = PageRequest.of(page, 10, sort);

        return productRepository.findProductByOrdered(sorted, category, size, color, search,
            pageable);
    }

    @Transactional
    public void calculateQuantity(PayAction action, Order order) {
      List<OrderDetail> orderDetailList =orderDetailRepository.findAllByOrderId(order.getId());
      if(orderDetailList.isEmpty()){
          throw new CustomException(ErrorType.NOT_FOUND_ORDER);
      }

      if(PayAction.BUY.equals(action)){
        for(OrderDetail orderDetail:orderDetailList){
          ProductDetail productDetail = productDetailRepository.findById(orderDetail.getProductDetail().getId()).orElseThrow(
              ()-> new CustomException(ErrorType.NOT_FOUND_PRODUCT_DETAIL));

          productDetail.updateQuantity(productDetail.getQuantity() - orderDetail.getCount());
          productDetailRepository.save(productDetail);
        }
      }
      else{
        for(OrderDetail orderDetail:orderDetailList){
          ProductDetail productDetail = productDetailRepository.findById(orderDetail.getProductDetail().getId()).orElseThrow(
              ()-> new CustomException(ErrorType.NOT_FOUND_PRODUCT_DETAIL));

          productDetail.updateQuantity(productDetail.getQuantity() + orderDetail.getCount());
          productDetailRepository.save(productDetail);
        }
      }

    }
}
