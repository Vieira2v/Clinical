package clinical.resource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import clinical.resource.repositories.model.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT u FROM UserEntity u WHERE u.username =:username")
    UserEntity findByUsername(@Param("username") String username);
}
