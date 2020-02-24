package villa.usman.projectmanagement.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import es.dmoral.toasty.Toasty;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class CommonMethods {
    ConnectivityManager connectivityManager;
    boolean connected = false;

    public boolean isEmail(String text) {
        CharSequence email = text;
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isOnline(Context context) {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            Toasty.error(context,"CheckConnectivity Exception: " + e.getMessage(), Toasty.LENGTH_LONG).show();
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public Date DateConvertFromString(String strdate, Context context){
        Date date = Calendar.getInstance().getTime();
        try{
            date = new SimpleDateFormat("yyyy-MM-DD").parse(strdate);
        }catch (Exception ex){
            Toasty.error(context,ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
        return date;
    }

    public static int getDiffYears(Date from, Date to) {
        Calendar start = getCalendar(from);
        Calendar end = getCalendar(to);
        int diff = end.get(YEAR) - start.get(YEAR);
        if (start.get(MONTH) > end.get(MONTH) ||
                (start.get(MONTH) == end.get(MONTH) && start.get(DATE) > end.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
