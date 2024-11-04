package clinical.resource.repositories;

import clinical.resource.repositories.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionEntityRepository extends JpaRepository<Permissions, Long> {
    @Query("SELECT u.id, u.fullName FROM UserEntity u JOIN u.permissions p WHERE p.id = :permissionId")
    List<String> findUserFullnamesByPermissionId(@Param("permissionId") Long permissionId);
}
