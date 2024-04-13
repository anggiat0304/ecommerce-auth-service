package com.ecommerce.authservice.respositoriy;

import com.ecommerce.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
    @Query(value = "SELECT r.name  FROM user u INNER JOIN user_roles ur  ON u.id  = ur.user_id INNER JOIN `role` r ON ur.role_id = r.id WHERE u.username  = :username",nativeQuery = true)
    List<String> findRoleByUserId(@Param("username")String username);

}
