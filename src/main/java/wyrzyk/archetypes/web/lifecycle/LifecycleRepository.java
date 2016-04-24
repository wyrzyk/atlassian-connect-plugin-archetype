package wyrzyk.archetypes.web.lifecycle;

import org.springframework.data.repository.CrudRepository;

interface LifecycleRepository extends CrudRepository<LifecycleEntity, Long> {
    LifecycleEntity findByClientKey(String clientKey);
}
