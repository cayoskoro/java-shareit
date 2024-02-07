package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookingRequestDto {
    private final Long itemId;
    @NotNull
    @FutureOrPresent
    private final LocalDateTime start;
    @NotNull
    @Future
    private final LocalDateTime end;

    @AssertTrue(message = "start after end")
    private boolean isStartDateBeforeEndDate() {
        return start != null && end != null && start.isBefore(end);
    }

    @AssertTrue(message = "start equal end")
    private boolean isNotStartEqualEnd() {
        return start != null && end != null && !start.isEqual(end);
    }
}
