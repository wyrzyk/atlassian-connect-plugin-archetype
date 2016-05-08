package wyrzyk.archetypes.lifecycle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wyrzyk.archetypes.api.ClientInfoDto;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Slf4j
public class LifecycleService {
    private final ClientInfoRepository clientInfoRepository;

    public Optional<ClientInfoDto> findClient(String clientKey) {
        final ClientInfoEntity clientEntity = clientInfoRepository.findByClientKey(clientKey);
        return ofNullable(clientEntity).map(ClientInfoEntity::toDto);
    }

    @Transactional
    ClientInfoDto save(ClientInfoEntity newClientInfoEntity) {
        final String clientKey = newClientInfoEntity.getClientKey();
        final ClientInfoEntity lifecycleEntity = clientInfoRepository.findByClientKey(clientKey);
        if (lifecycleEntity != null) {
            log.debug("Plugin has been installed again for user {}", clientKey);
            newClientInfoEntity.setId(lifecycleEntity.getId());
        } else {
            log.debug("Plugin has been installed for user {}", clientKey);
        }
        newClientInfoEntity.setInstalled(true);
        final ClientInfoEntity entity = clientInfoRepository.save(newClientInfoEntity);
        return entity.toDto();
    }

    boolean setInstalled(String clientKey, boolean installed) {
        final boolean isUpdated = clientInfoRepository.setInstalled(clientKey, installed) > 0;
        if (isUpdated) {
            log.debug("Installed flag has been changed to {} for {}", installed, clientKey);
        } else {
            log.warn("Faild to change installed flag to {} for {}", installed, clientKey);
        }
        return isUpdated;
    }

    boolean setEnabled(String clientKey, boolean enabled) {
        final boolean isUpdated = clientInfoRepository.setEnabled(clientKey, enabled) > 0;
        if (isUpdated) {
            log.debug("Enabled flag has been changed to {} for {}", enabled, clientKey);
        } else {
            log.warn("Faild to change enabled flag to {} for {}", enabled, clientKey);
        }
        return isUpdated;
    }

    private boolean isEnabled(String clientKey) {
        final Boolean enabled = clientInfoRepository.isEnabled(clientKey);
        return enabled != null && enabled;
    }

    private boolean isInstalled(String clientKey) {
        final Boolean installed = clientInfoRepository.isInstalled(clientKey);
        return installed != null && installed;
    }

    private long countClients() {
        return clientInfoRepository.count();
    }
}
