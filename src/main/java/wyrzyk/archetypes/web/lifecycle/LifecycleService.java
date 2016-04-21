package wyrzyk.archetypes.web.lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LifecycleService {
    private final LifecycleRepository lifecycleRepository;

    @Autowired
    public LifecycleService(LifecycleRepository lifecycleRepository) {
        this.lifecycleRepository = lifecycleRepository;
    }

    @Transactional
    LifecycleDto save(LifecycleDto lifecycleDto) {
        final LifecycleInstallEntity lifecycleInstallEntity = LifecycleInstallEntity.fromDto(lifecycleDto);
        final LifecycleInstallEntity entity = lifecycleRepository.save(lifecycleInstallEntity);
        return entity.toDto();
    }
}
