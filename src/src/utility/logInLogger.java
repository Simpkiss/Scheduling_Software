package utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/** Logger that writes to a text file.
 *
 */
public class logInLogger {
    /** Location and name of activity log.
     *
     */
    private static final String path = "login_activity.txt";

    /** Writes to activity log when called and specifies if attempt was successful or not, the username, and the timestamp of the attempt.
     *
     */
    public static void newLog(String userName, Boolean success) {
        try {
            BufferedWriter logger = new BufferedWriter(new FileWriter(path, true));
            String successText;
            if(success){
                successText = "Successful ";
            } else{
                successText = "Failed ";
            }
            logger.append(successText + "log in by " + userName + " at " + ZonedDateTime.now(ZoneOffset.UTC) + "\n");
            logger.flush();
            logger.close();
        }
        catch (IOException error) {
            error.printStackTrace();
        }
    }
}