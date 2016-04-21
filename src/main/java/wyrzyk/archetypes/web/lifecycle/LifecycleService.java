package wyrzyk.archetypes.web.lifecycle;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor=@__({@Autowired}))
public class LifecycleService {
    private final LifecycleRepository lifecycleRepository;

    @Transactional
    LifecycleDto save(LifecycleDto lifecycleDto) {
        final LifecycleInstallEntity lifecycleInstallEntity = LifecycleInstallEntity.fromDto(lifecycleDto);
        final LifecycleInstallEntity entity = lifecycleRepository.save(lifecycleInstallEntity);
        return entity.toDto();
    }
}
