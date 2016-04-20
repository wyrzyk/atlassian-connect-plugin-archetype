package wyrzyk.archetypes.web.resources.lifecycle;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "lifecycle")
public class LifecycleResource {
    @RequestMapping(value = "installed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)

    public ResponseEntity<Void> installed(@RequestBody LifecycleRequest lifecycleRequest) {
        return ResponseEntity.ok().build();
    }
}
