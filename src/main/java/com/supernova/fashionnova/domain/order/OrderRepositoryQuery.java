package com.supernova.fashionnova.domain.order;

import java.util.Optional;

public interface OrderRepositoryQuery {

    Optional<Long> findTodayOrderTotalPriceSum();

    Optional<Long> findWeekOrderTotalPriceSum();

    Optional<Long> findMonthOrderTotalPriceSum(int month);

}
