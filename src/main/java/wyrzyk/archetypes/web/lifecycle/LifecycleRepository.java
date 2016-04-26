package wyrzyk.archetypes.web.lifecycle;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

interface LifecycleRepository extends CrudRepository<LifecycleEntity, Long> {
    LifecycleEntity findByClientKey(String clientKey);

    @Modifying
    @Query("UPDATE LifecycleEntity l SET l.installed = ?2 WHERE l.clientKey = ?1")
    int setInstalled(String clientKey, boolean installed);

    @Modifying
    @Query("UPDATE LifecycleEntity l SET l.enabled = ?2 WHERE l.clientKey = ?1")
    int setEnabled(String clientKey, boolean enabled);

    @Query("SELECT l.enabled FROM  LifecycleEntity l WHERE l.clientKey = ?1")
    boolean isEnabled(String clientKey);

    @Query("SELECT l.installed FROM  LifecycleEntity l WHERE l.clientKey = ?1")
    boolean isInstalled(String clientKey);
}
