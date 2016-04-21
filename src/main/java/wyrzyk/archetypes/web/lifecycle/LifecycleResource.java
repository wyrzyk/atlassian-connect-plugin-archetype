package wyrzyk.archetypes.web.lifecycle;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "lifecycle")
class LifecycleResource {
    private final LifecycleService lifecycleService;

    @Autowired
    LifecycleResource(LifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    @RequestMapping(value = "installed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    public ResponseEntity<Void> installed(@RequestBody LifecycleInstallRequest lifecycleRequest) {
        final LifecycleDto lifecycleDto = lifecycleService.save(lifecycleRequest.toDto());
        if(lifecycleDto.getId() != null){   // todo: fix error handling
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
