package com.supernova.fashionnova.address;

import com.supernova.fashionnova.user.User;
import java.util.List;

public interface AddressRepositoryQuery {

    List<Address> findByUserOrderByDefaultAddressDesc(User user);
    void updateDefaultAddress(Long userId, Long addressId);

}
