
package com.devdad.Forma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devdad.Forma.model.Address;
import com.devdad.Forma.model.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findAllByUser(User user);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId")
    List<Address> findAllByUserId(@Param("userId") String userId);
}
