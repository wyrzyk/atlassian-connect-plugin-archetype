package wyrzyk.archetypes.web.lifecycle;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
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
        final LifecycleEntity entity = lifecycleRepository.save(newLifecycleEntity);
        return entity.toDto();
    }

    @Transactional
    boolean isEnabled(String clientKey) {
        throw new NotImplementedException("not implemented yet");
    }

    @Transactional
    boolean isInstalled(String clientKey) {
        throw new NotImplementedException("not implemented yet");
    }
}
