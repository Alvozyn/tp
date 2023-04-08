package seedu.address.model.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.SampleDateTimeUtil.FIVE_O_CLOCK_VALID;
import static seedu.address.testutil.SampleDateTimeUtil.MONDAY_SIX_O_CLOCK_VALID;
import static seedu.address.testutil.SampleDateTimeUtil.NINE_O_CLOCK_VALID;
import static seedu.address.testutil.SampleDateTimeUtil.THREE_O_CLOCK_VALID;
import static seedu.address.testutil.SampleDateTimeUtil.THURSDAY_TWELVE_O_CLOCK_VALID;
import static seedu.address.testutil.SampleDateTimeUtil.TWELVE_O_CLOCK_VALID;
import static seedu.address.testutil.SampleDateTimeUtil.TWO_O_CLOCK_VALID;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class RecurringEventListTest {

    private final RecurringEventList recurringEventList = new RecurringEventList();

    private class RecurringEventStub extends RecurringEvent {

        public RecurringEventStub(String eventName, DayOfWeek day, LocalTime startTime, LocalTime endTime) {
            super(eventName, day, startTime, endTime);
        }

        public int compareTo(RecurringEvent recurringEvent2) {
            return super.compareTo(recurringEvent2);
        }

    }

    @Test
    void insert_nullEvent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> recurringEventList.insert(null));
    }

    @Test
    void contain() {
        recurringEventList.insert(new RecurringEventListTest.RecurringEventStub("Biking", DayOfWeek.MONDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));
        recurringEventList.insert(new RecurringEventListTest.RecurringEventStub("Skiing", DayOfWeek.MONDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));
        recurringEventList.insert(new RecurringEventListTest.RecurringEventStub("Canoeing", DayOfWeek.MONDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));

        assertFalse(recurringEventList.contain(new RecurringEventListTest.RecurringEventStub("Biking",
                DayOfWeek.WEDNESDAY, TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime())));

        assertTrue(recurringEventList.contain(new RecurringEventListTest.RecurringEventStub("Canoeing",
                DayOfWeek.MONDAY, TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime())));
    }

    @Test
    void testToString() {
        recurringEventList.insert(new RecurringEventStub("Biking", DayOfWeek.MONDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Skiing", DayOfWeek.SATURDAY,
                NINE_O_CLOCK_VALID.toLocalTime(), TWELVE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Canoeing", DayOfWeek.WEDNESDAY,
                TWELVE_O_CLOCK_VALID.toLocalTime(), FIVE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Invalid", DayOfWeek.MONDAY,
                TWELVE_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));

        assertEquals("Recurring Events\n" + "1. Biking on MONDAY from 14:00 to 15:00\n" +
                "2. Canoeing on WEDNESDAY from 12:00 to 17:00\n" +
                "3. Skiing on SATURDAY from 09:00 to 12:00\n", recurringEventList.toString());
    }

    @Test
    void testListBetweenOcurrence() {
        recurringEventList.insert(new RecurringEventStub("Biking", DayOfWeek.MONDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Skiing", DayOfWeek.SATURDAY,
                NINE_O_CLOCK_VALID.toLocalTime(), TWELVE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Canoeing", DayOfWeek.WEDNESDAY,
                TWELVE_O_CLOCK_VALID.toLocalTime(), FIVE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Invalid", DayOfWeek.MONDAY,
                TWELVE_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));

        LocalDateTime startPeriod = MONDAY_SIX_O_CLOCK_VALID;
        LocalDateTime endPeriod = THURSDAY_TWELVE_O_CLOCK_VALID;

        assertEquals("Biking on MONDAY from 14:00 to 15:00\nCanoeing on WEDNESDAY from 12:00 to 17:00\n",
                recurringEventList.listBetweenOccurrence(startPeriod, endPeriod));
    }


    @Test
    void testgetRecurringEvent() {
        recurringEventList.insert(new RecurringEventStub("Biking", DayOfWeek.MONDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Canoeing", DayOfWeek.WEDNESDAY,
                TWELVE_O_CLOCK_VALID.toLocalTime(), FIVE_O_CLOCK_VALID.toLocalTime()));

        recurringEventList.insert(new RecurringEventStub("Skiing", DayOfWeek.SATURDAY,
                NINE_O_CLOCK_VALID.toLocalTime(), TWELVE_O_CLOCK_VALID.toLocalTime()));

        assertEquals("Biking on MONDAY from 14:00 to 15:00",
                recurringEventList.getRecurringEvent(0).toString());

        assertEquals("Canoeing on WEDNESDAY from 12:00 to 17:00",
                recurringEventList.getRecurringEvent(1).toString());

        assertEquals("Skiing on SATURDAY from 09:00 to 12:00",
                recurringEventList.getRecurringEvent(2).toString());

    }

    @Test
    void testEdit() {
        RecurringEvent event = new RecurringEventStub("Skiing", DayOfWeek.WEDNESDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), THREE_O_CLOCK_VALID.toLocalTime());
        recurringEventList.insert(event);
        RecurringEvent editedEvent = new RecurringEventStub("Skiing", DayOfWeek.MONDAY,
                TWO_O_CLOCK_VALID.toLocalTime(), FIVE_O_CLOCK_VALID.toLocalTime());
        recurringEventList.edit(event, editedEvent);
        assertEquals(recurringEventList.getRecurringEvent(0).toString(), editedEvent.toString());
    }

//    @Test
//    void checkOverlapping_throwsEventConflictException() {
//        recurringEventList.insert(new RecurringEventStub(("Biking", DayOfWeek.MONDAY, TWO_O_CLOCK_VALID,
//                THREE_O_CLOCK_VALID));
//        isolatedEventList.insert(new IsolatedEventListTest.IsolatedEventStub("Skiing", TWO_O_CLOCK_VALID,
//                THREE_O_CLOCK_VALID));
//        isolatedEventList.insert(new IsolatedEventListTest.IsolatedEventStub("Canoeing", TWO_O_CLOCK_VALID,
//                THREE_O_CLOCK_VALID));
//        IsolatedEventListTest.IsolatedEventStub event = new IsolatedEventListTest.IsolatedEventStub("Biking", TWO_O_CLOCK_VALID, THREE_O_CLOCK_VALID);
//        assertThrows(EventConflictException.class, () -> isolatedEventList.checkOverlapping(event, 1));
//    }

}
