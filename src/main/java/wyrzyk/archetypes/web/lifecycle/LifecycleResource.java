package wyrzyk.archetypes.web.lifecycle;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "lifecycle", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class LifecycleResource {
    private final LifecycleService lifecycleService;

    @RequestMapping(value = "installed")
    public ResponseEntity<Void> installed(@RequestBody LifecycleRequest lifecycleRequest) {
        final LifecycleDto lifecycleDto = lifecycleService.save(lifecycleRequest.toDto());
        if (lifecycleDto.getId() != null) {   // todo: fix error handling
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "enabled")
    public ResponseEntity<Void> enabled(@RequestBody LifecycleRequest lifecycleRequest) {
        return lifecycleService.setEnabled(lifecycleRequest.getClientKey(), true) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "disabled")
    public ResponseEntity<Void> disabled(@RequestBody LifecycleRequest lifecycleRequest) {
        return lifecycleService.setEnabled(lifecycleRequest.getClientKey(), false) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "uninstalled")
    public ResponseEntity<Void> uninstalled(@RequestBody LifecycleRequest lifecycleRequest) {
        return lifecycleService.setInstalled(lifecycleRequest.getClientKey(), false) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
