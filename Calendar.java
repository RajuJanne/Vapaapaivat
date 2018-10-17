package RajuJanne.vp;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Janne Rajuvaara, NTK17SP
 */


@SuppressWarnings("OptionalGetWithoutIsPresent")
class Calendar {

    private int [][] _calendar;
    private int _offdays;
    private int _normalOff;
    private int _specialOff;
    private int _maxStint;
    private ArrayList<Object> _breakpoints;
    private ArrayList<Object> _workErrors;
    private ArrayList<Object> _dayErrors;

    Calendar(int we, int wo, int od, int no, int so, int ms)
    {
        // luodaan työntekijöiden array, joista jokaiselle annetaan arvoksi päivien array
        // luodaan kaksi ylimääräistä ylemmän tason arraytä, joihin talletetaan virhetietoja
        _calendar = new int[wo + 2][we * 7];
        // sijoitetaan jokaisen alkion arvoksi 0, eli työpäivä. ks. seedCalendar()
        for (int i = 0; i < _calendar.length; i++) {
            for (int j = 0; j < _calendar[i].length; j++) {
                _calendar[i][j] = 0;
            }
        }
        _offdays = od;
        _normalOff = no;
        _specialOff = so;
        _maxStint = ms;
    }

/*
public int[][] get_calendar () {
return _calendar;
}
*/

    public ArrayList<Object> get_breakpoints() {
        return _breakpoints;
    }

    void seedCalendar(int x) { // arvotaan jokaiselle tekijälle x määrä vapaapäiviä
        for (int i = 0; i < _calendar.length - 2; i++) {
            int j = 0;
            while (j < x)
            {
                Random r = new Random();
                int arvottu = r.ints(0, (_calendar[i].length)).findFirst().getAsInt();

                if(_calendar[i][arvottu] != 1)
                {
                    _calendar[i][arvottu] = 1;
                    j++;
                }
            }
        }
    }

    void getWorkerErrors()
    {

        for (int i = 0; i < _calendar.length - 2; i++) {
            int val = 0;
            for (int j = 0; j < _calendar[i].length ;j++)
            {
                if (_calendar[i][j] == 1) {
                    val++;
                }
            }
            // sijoitetaan toiseksiviimeiselle riville jokaisen pystysarakkeen offsetti.
            _calendar[_calendar.length - 2][i] = val - _offdays;
        }
    }

    void getDayErrors() {
        // palauttaa päivän lomailijoiden määrän ja halutun määrän erotuksen
        // positiivinen = liikaa työläisiä vapaalla. negatiivinen = liian vähän


        for (int i = 0; i < _calendar.length - 2; i++) {

            for (int j = 0; j < _calendar[i].length; j++) {
                // jokaisen työntekijän i päivä j käydään läpi, ja vapaapäivän löytyessä nostetaan "val" arvoa yhdellä
                if (_calendar[i][j] == 1)
                {
                    _calendar[_calendar.length -1][j]++;
                }
            }
        }
    }

    void calculateDayOffDifferentials() {
        for (int j = 0; j < _calendar[_calendar.length -1].length; j++)
        {
            if (j < 7) {
                if (j == 5 || j == 6) // päivä on jaollinen 5 tai 6 = lauantai tai sunnuntai
                {
                    _calendar[_calendar.length -1][j] = _calendar[_calendar.length -1][j] - _specialOff;
                }
                else {
                    _calendar[_calendar.length -1][j] = _calendar[_calendar.length -1][j] - _normalOff;
                }

            } else if ((j - 5) % 7 == 0 || (j - 6) % 7 == 0) {
                // päivä miinus 5 tai 6 on jaollinen 7:llä = toinen ja sitä seuraavat viikonloput
                {
                    _calendar[_calendar.length -1][j] = _calendar[_calendar.length -1][j] - _specialOff;
                }
            } else {
                _calendar[_calendar.length -1][j] = _calendar[_calendar.length -1][j] - _normalOff;
            }
        }
    }

    // Pseudokoodi stinteille:
    // if (breakpoints.contains(i)) { combo = 0; continue; } if (cal[x][i] == cal[x][i+1]) ? combo++; : combo = 0; if (combo == 7) breakpoints.Add(i+1);
    // kurkistaa aina yhden eteenpäin, eli ajavan loopin määrittelyssä pitää muistaa lopettaa loopin suoritus yhtä aikaisemmin.

    void getStintBreakpoints() {
        ArrayList<Object> allBreakPoints = new ArrayList<>();
        for (int j = 0; j < _calendar.length - 2; j++)
        {
            int combo = 0;
            ArrayList<Integer> workerBreakPoints = new ArrayList<>();
            for (int i = 0;i < _calendar[j].length - 1; i++)
            {
                if (workerBreakPoints.contains(i)) {
                    combo = 0;
                    continue; // hyppää lähimmän isäntäloopin seuraavaan iteraatioon. miksi? koska lookahead ja tehokkuus.
                }
                else if (_calendar[j][i] == _calendar[j][i+1]) {
                    combo++;
                }
                else {
                    combo = 0;
                }
                if (combo == _maxStint)
                {
                    // length arvo on aina sama kuin suurin indeksi + 1
                    workerBreakPoints.add(i+1);
                }
            }
            allBreakPoints.add(workerBreakPoints);
        }
        _breakpoints = allBreakPoints;
    }

    void fixWorstDays() {
        // pitäisi tallentaa indeksinumero perkele!
        ArrayList<Integer> smallestValue = new ArrayList<>();
        ArrayList<Integer> highestValue = new ArrayList<>();
        smallestValue.add(_calendar[_calendar.length-1][0]);
        for (int i = 1; i < _calendar[_calendar.length-1].length; i++)
        {
            // tää alkaa näyttää sellaselta vitun avaruusoopperalta taas et ei vittu
            // jos viimeisen rivin indeksin i arvo on pienempi kuin smallestValue listalle talletetun indeksin arvo, niin alustetaan lista uusiksi ja lisätään sinne indeksi i.
            if (_calendar[_calendar.length-1][i] < _calendar[_calendar.length-1][smallestValue.get(0)]) {
                smallestValue = new ArrayList<>();
                smallestValue.add(i);
            } else if (_calendar[_calendar.length-1][i] == _calendar[_calendar.length-1][smallestValue.get(0)]) {
                smallestValue.add(i);
            }
        }
        highestValue.add(_calendar[_calendar.length-1][0]);
        for (int i = 1; i < _calendar[_calendar.length-1].length; i++) {
            if (_calendar[_calendar.length - 1][i] > _calendar[_calendar.length - 1][highestValue.get(0)]) {
                highestValue = new ArrayList<>();
                highestValue.add(i);
            } else if (_calendar[_calendar.length - 1][i] == _calendar[_calendar.length - 1][highestValue.get(0)]) {
                highestValue.add(i);
            }
        }
        // valitaan satunnainen matalin ja korkein
        int small = smallestValue.get(new Random().nextInt(smallestValue.size()));
        int high = highestValue.get(new Random().nextInt(highestValue.size()));

        // seuraavaksi haetaan smallest indeksillä työntekijä, jolla on työpäivä, laitetaan se vapaalle,
        // ja annetaan samalle hemmolle vapaapäivä highest indeksillä, mikäli sillä ei ole jo.
        // todennäköisesti heitetään uuteen funktioon tämä käsittely.
    }

    void fixWorstWorkers() {

    }

    void ToString() {
        for (int[] _calendar1 : _calendar) {
            System.out.println(Arrays.toString(_calendar1));
        }
    }
}
