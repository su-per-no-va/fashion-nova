package com.supernova.fashionnova.address;

import com.supernova.fashionnova.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long>  {
    List<Address> findByUser(User user);
}
