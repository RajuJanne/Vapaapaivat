package RajuJanne.vp;

import java.util.Random;

/**
 * @author Janne Rajuvaara, NTK17SP
 */

public class Calendar {

    private int [][] _calendar;

    public Calendar(int we, int wo)
    {
        for (int i = 0; i < wo; i++) {
            _calendar[wo] = new int[we * 7];
            // jokaiselle tekijälle viikot * 7 alkiota = kaikki päivät
        }
    }

    public int[][] get_calendar () {
        return _calendar;
    }

    public void seedCalendar(int x) { // arvotaan jokaiselle tekijälle x määrä vapaapäiviä
        for (int i = 0; i < _calendar.length; i++) {
            int j = 0;
            while (j < x)
            {
                Random r = new Random();
                int arvottu = r.ints(0, (_calendar[i].length+1)).findFirst().getAsInt();
                if(_calendar[i][arvottu] != 1)
                {
                    _calendar[i][arvottu] = 1;
                    j++;
                }
            }
        }
    }
}