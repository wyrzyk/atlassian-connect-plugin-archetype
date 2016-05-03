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
    private final ClientInfoRepository clientInfoRepository;

    @Transactional
    ClientInfoDto save(ClientInfoDto clientInfoDto) {
        final ClientInfoEntity lifecycleEntity = clientInfoRepository.findByClientKey(clientInfoDto.getClientKey());
        final ClientInfoEntity newClientInfoEntity = ClientInfoEntity.fromDto(clientInfoDto);
        if (lifecycleEntity != null) {
            log.debug("Plugin has been installed again for user {}", clientInfoDto.getClientKey());
            newClientInfoEntity.setId(lifecycleEntity.getId());
        } else {
            log.debug("Plugin has been installed for user {}", clientInfoDto.getClientKey());
        }
        newClientInfoEntity.setInstalled(true);
        final ClientInfoEntity entity = clientInfoRepository.save(newClientInfoEntity);
        return entity.toDto();
    }

    @Transactional
    public boolean isEnabled(String clientKey) {
        final Boolean enabled = clientInfoRepository.isEnabled(clientKey);
        return enabled != null && enabled;
    }

    @Transactional
    public boolean isInstalled(String clientKey) {
        final Boolean installed = clientInfoRepository.isInstalled(clientKey);
        return installed != null && installed;
    }

    @Transactional
    public boolean setInstalled(String clientKey, boolean installed) {
        final boolean isUpdated = clientInfoRepository.setInstalled(clientKey, installed) > 0;
        if (isUpdated) {
            log.debug("Installed flag has been changed to {} for {}", installed, clientKey);
        } else {
            log.warn("Faild to change installed flag to {} for {}", installed, clientKey);
        }
        return isUpdated;
    }

    @Transactional
    public boolean setEnabled(String clientKey, boolean enabled) {
        final boolean isUpdated = clientInfoRepository.setEnabled(clientKey, enabled) > 0;
        if (isUpdated) {
            log.debug("Enabled flag has been changed to {} for {}", enabled, clientKey);
        } else {
            log.warn("Faild to change enabled flag to {} for {}", enabled, clientKey);
        }
        return isUpdated;
    }

    public long countClients() {
        return clientInfoRepository.count();
    }

    public Optional<ClientInfoDto> findClient(String clientKey) {
        final ClientInfoEntity clientEntity = clientInfoRepository.findByClientKey(clientKey);
        return Optional.ofNullable(clientEntity).map(ClientInfoEntity::toDto);
    }
}
