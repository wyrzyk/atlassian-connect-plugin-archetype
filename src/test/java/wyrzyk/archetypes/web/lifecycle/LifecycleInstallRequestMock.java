package wyrzyk.archetypes.web.lifecycle;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class LifecycleInstallRequestMock {
    String key;
}
