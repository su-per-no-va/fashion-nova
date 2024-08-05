package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetail(Long orderId, User user) {

        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(orderId);

        if (orderDetailList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_ORDER);
        }

        if (!user.getId().equals(orderDetailList.get(0).getUser().getId())) {
            throw new CustomException(ErrorType.DENIED_PERMISSION);
        }

        return orderDetailList;
    }

}
