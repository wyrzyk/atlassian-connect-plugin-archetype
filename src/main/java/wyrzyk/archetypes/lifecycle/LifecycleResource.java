package wyrzyk.archetypes.lifecycle;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static wyrzyk.archetypes.lifecycle.ClientInfoEntity.fromDto;


@RestController
@RequestMapping(value = "lifecycle", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class LifecycleResource {
    private final LifecycleService lifecycleService;

    @RequestMapping(value = "installed")
    public ResponseEntity<Void> installed(@RequestBody ClientInfoRequest clientInfoRequest) {
        final ClientInfoDtoImpl lifecycleDto = lifecycleService.save(fromDto(clientInfoRequest.toDto()));
        if (lifecycleDto.getId() != null) {   // todo: fix error handling
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "enabled")
    public ResponseEntity<Void> enabled(@RequestBody ClientInfoRequest clientInfoRequest) {
        return lifecycleService.setEnabled(clientInfoRequest.getClientKey(), true) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "disabled")
    public ResponseEntity<Void> disabled(@RequestBody ClientInfoRequest clientInfoRequest) {
        return lifecycleService.setEnabled(clientInfoRequest.getClientKey(), false) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "uninstalled")
    public ResponseEntity<Void> uninstalled(@RequestBody ClientInfoRequest clientInfoRequest) {
        return lifecycleService.setInstalled(clientInfoRequest.getClientKey(), false) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
