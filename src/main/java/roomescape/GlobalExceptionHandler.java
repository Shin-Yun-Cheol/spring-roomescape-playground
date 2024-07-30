package roomescape;

import roomescape.exception.InvalidReservationException;
import roomescape.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e) {
        System.out.println("NotFoundException occurred: " + e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<String> handleInvalidReservationException(InvalidReservationException e) {
        System.out.println("InvalidReservationException occurred: " + e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
