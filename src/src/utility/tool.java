package utility;

import database.appointmentDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/** A few tools that can be called from other classes in order to keep their code more simple. */
public class tool {

    /** Generates a list of valid start times for an appointment at 15 minute intervals. Skips any times a given customer already has an existing appointment unless the existing appointment is the given appointment.
     * This lets start times between the start and end of a selected appointment be valid when updating an appointment.
     *
     */
    public static ObservableList<String> smartStart(OffsetTime openTime, OffsetTime closeTime, LocalDate selectedDate,int customerID, int apptID) throws SQLException {
        DateTimeFormatter ampm = DateTimeFormatter.ofPattern("h:mm a");
        ObservableList<String> aptTimes = FXCollections.observableArrayList();
        OffsetTime time = openTime;
        LocalDateTime lookUpTime = LocalDateTime.of(selectedDate, time.toLocalTime());
        while (time.isBefore(closeTime)) {
            if (appointmentDB.appointmentConflict(customerID, lookUpTime) <= 0 || appointmentDB.appointmentConflict(customerID, lookUpTime) == apptID) {
                aptTimes.add(time.format(ampm));
            }
            time = time.plusMinutes(15);
            lookUpTime = lookUpTime.plusMinutes(15);
        }
        return aptTimes;
    }

    /** Generates a list of valid end times for an appointment at 15 minute intervals after the start time.
     * Stops at either the business close time or beginning of the customers next appointment that does not have the given appointment ID.
     * This lets end times between the start and end of a selected appointment be valid when updating an appointment.
     *
     */
    public static ObservableList<String> smartEnd(OffsetTime startTime, OffsetTime closeTime, LocalDate selectedDate,int customerID, int apptID) throws SQLException {
        DateTimeFormatter ampm = DateTimeFormatter.ofPattern("h:mm a");
        ObservableList<String> aptTimes = FXCollections.observableArrayList();
        OffsetTime time = startTime;
        LocalDateTime lookUpTime = LocalDateTime.of(selectedDate, time.toLocalTime());
        while (time.isBefore(closeTime) && (appointmentDB.appointmentNext(customerID, lookUpTime) < 0 || appointmentDB.appointmentNext(customerID, lookUpTime) ==apptID)) {
            time = time.plusMinutes(15);
            lookUpTime = lookUpTime.plusMinutes(15);
            aptTimes.add(time.format(ampm));
        }
        return aptTimes;
    }

    /** Generates a string translation of the duration between the start and end of an appointment that is easy to read and correctly pluralizes and omits words as necessary.
     *
     */
    public static String lengthText(LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long durMins = duration.toMinutes();
        String hour = "";
        String mins = "";
        if (durMins >= 60) {
            if (durMins >= 120) {
                hour = duration.toHours() + " hours ";
            } else {
                hour = "1 hour ";
            }
            if (durMins - (duration.toHours() * 60) > 0) {
                mins = (durMins - (duration.toHours() * 60)) + " minutes";
            }
        } else {
            mins = durMins + " minutes";
        }
        return hour + mins;
    }
}

