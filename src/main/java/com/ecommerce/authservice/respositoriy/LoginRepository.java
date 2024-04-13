package com.ecommerce.authservice.respositoriy;

import com.ecommerce.authservice.model.Login;
import com.ecommerce.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LoginRepository extends JpaRepository<Login ,Long> {
    @Query(value = "SELECT * FROM login WHERE user_id = :userId",nativeQuery = true)
    Login findLoginByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM login WHERE token = :token AND user_id = :userId",nativeQuery = true)
    Login findLoginByTokenAndAndUserId(@Param("token") String token , @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM login WHERE token = :token AND user_id = :userId", nativeQuery = true)
    void deleteLoginByTokenAndUserId(@Param("token") String token, @Param("userId") Long userId);
}
