package wyrzyk.archetypes.web.lifecycle;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class LifecycleService {
    private final LifecycleRepository lifecycleRepository;

    @Transactional
    LifecycleDto save(LifecycleDto lifecycleDto) {
        final LifecycleEntity lifecycleEntity = lifecycleRepository.findByClientKey(lifecycleDto.getClientKey());
        final LifecycleEntity newLifecycleEntity = LifecycleEntity.fromDto(lifecycleDto);
        if (lifecycleEntity != null) {
            newLifecycleEntity.setId(lifecycleEntity.getId());
        }
        newLifecycleEntity.setInstalled(true);
        final LifecycleEntity entity = lifecycleRepository.save(newLifecycleEntity);
        return entity.toDto();
    }

    @Transactional
    boolean isEnabled(String clientKey) {
        return lifecycleRepository.isEnabled(clientKey);
    }

    @Transactional
    boolean isInstalled(String clientKey) {
        return lifecycleRepository.isInstalled(clientKey);
    }

    @Transactional
    boolean setInstalled(String clientKey, boolean installed) {
        return lifecycleRepository.setInstalled(clientKey, installed) > 0;
    }

    @Transactional
    boolean setEnabled(String clientKey, boolean enabled) {
        return lifecycleRepository.setEnabled(clientKey, enabled) > 0;
    }
}
