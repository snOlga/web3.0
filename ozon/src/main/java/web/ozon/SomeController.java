package web.ozon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/some")
public class SomeController {

    @GetMapping("/")
    public ResponseEntity<String> firstMethod() {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }
}
