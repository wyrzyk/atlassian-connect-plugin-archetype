package wyrzyk.archetypes.web.lifecycle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class LifecycleService {
    private final LifecycleRepository lifecycleRepository;

    @Transactional
    LifecycleDto save(LifecycleDto lifecycleDto) {
        final LifecycleEntity lifecycleEntity = lifecycleRepository.findByClientKey(lifecycleDto.getClientKey());
        final LifecycleEntity newLifecycleEntity = LifecycleEntity.fromDto(lifecycleDto);
        if (lifecycleEntity != null) {
            log.debug("Plugin has been installed again for user {}", lifecycleDto.getClientKey());
            newLifecycleEntity.setId(lifecycleEntity.getId());
        } else {
            log.debug("Plugin has been installed for user {}", lifecycleDto.getClientKey());
        }
        newLifecycleEntity.setInstalled(true);
        final LifecycleEntity entity = lifecycleRepository.save(newLifecycleEntity);
        return entity.toDto();
    }

    @Transactional
    public boolean isEnabled(String clientKey) {
        return lifecycleRepository.isEnabled(clientKey);
    }

    @Transactional
    public boolean isInstalled(String clientKey) {
        return lifecycleRepository.isInstalled(clientKey);
    }

    @Transactional
    boolean setInstalled(String clientKey, boolean installed) {
        final boolean isUpdated = lifecycleRepository.setInstalled(clientKey, installed) > 0;
        if (isUpdated) {
            log.debug("Installed flag has been changed to {} for {}", installed, clientKey);
        } else {
            log.warn("Faild to change installed flag to {} for {}", installed, clientKey);
        }
        return isUpdated;
    }

    @Transactional
    boolean setEnabled(String clientKey, boolean enabled) {
        final boolean isUpdated = lifecycleRepository.setEnabled(clientKey, enabled) > 0;
        if (isUpdated) {
            log.debug("Enabled flag has been changed to {} for {}", enabled, clientKey);
        } else {
            log.warn("Faild to change enabled flag to {} for {}", enabled, clientKey);
        }
        return isUpdated;
    }

    public long countClients() {
        return lifecycleRepository.count();
    }

    public Optional<LifecycleDto> findClient(String clientKey) {
        final LifecycleEntity clientEntity = lifecycleRepository.findByClientKey(clientKey);
        return Optional.ofNullable(clientEntity.toDto());
    }
}
