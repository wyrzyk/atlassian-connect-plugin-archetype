package wyrzyk.archetypes.web.resources.lifecycle;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class LifecycleRequestMock {
    String key;
}
