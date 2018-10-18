package RajuJanne.vp;

import java.lang.reflect.Array;
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
    private int _small;
    private int _high;
    private Boolean _worstDaysSet;

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
        int lastLine = _calendar.length - 1;
        // nollaa edelliset virheet.
        _calendar[lastLine] = new int[_calendar[lastLine].length];

        for (int i = 0; i < _calendar.length - 2; i++) {

            for (int j = 0; j < _calendar[i].length; j++) {
                // jokaisen työntekijän i päivä j käydään läpi, ja vapaapäivän löytyessä nostetaan "val" arvoa yhdellä
                if (_calendar[i][j] == 1)
                {
                    _calendar[lastLine][j]++;
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
        if(get_breakpoints() != null) get_breakpoints().clear();
        ArrayList<Object> allBreakPoints = new ArrayList<>();
        for (int j = 0; j < _calendar.length - 2; j++)
        {
            int combo = 0;
            ArrayList<Integer> workerBreakPoints = new ArrayList<Integer>();
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
 /*
    void fixStints() {
        if(get_breakpoints() != null) {
            for (int i = 0; i < get_breakpoints().size(); i++) {
                if (!get_breakpoints().get(i).toString().equals("[]"))
                {
                    int pilkku = get_breakpoints().get(i).toString().indexOf(',');
                    int a = Integer.parseInt(get_breakpoints().get(i).toString().substring(pilkku - 1,pilkku));
                    // To be continued
                }
            }
        }
    }
*/
    void findWorstDays() {
        // pitäisi tallentaa indeksinumero perkele!
        ArrayList<Integer> smallestValue = new ArrayList<>();
        ArrayList<Integer> highestValue = new ArrayList<>();
        smallestValue.add(0);
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
        highestValue.add(0);
        for (int i = 1; i < _calendar[_calendar.length-1].length; i++) {
            if (_calendar[_calendar.length - 1][i] > _calendar[_calendar.length - 1][highestValue.get(0)]) {
                highestValue = new ArrayList<>();
                highestValue.add(i);
            } else if (_calendar[_calendar.length - 1][i] == _calendar[_calendar.length - 1][highestValue.get(0)]) {
                highestValue.add(i);
            }
        }
        // valitaan satunnainen matalin ja korkein -> tallennetaan kenttämuuttujiin.
        _small = smallestValue.get(new Random().nextInt(smallestValue.size()));
        _high = highestValue.get(new Random().nextInt(highestValue.size()));
        _worstDaysSet = true;

        // seuraavaksi täytyy hakea smallest indeksillä työntekijä, jolla on työpäivä, laitetaan se vapaalle,
        // ja annetaan samalle hemmolle vapaapäivä highest indeksillä, mikäli sillä ei ole jo.
        // todennäköisesti heitetään uuteen funktioon tämä käsittely.
    }

    void swapWorstDays() {
        //if (!_worstDaysSet) fixWorstWorkers();

        // eli nyt pitäis löytää i-indeksi, jolla on j=high indeksi, muttei j=small ja siirtää high->small
        for (int[] worker : _calendar) {
            if (worker[_high] == 1 && worker[_small] == 0) {
                worker[_high] = 0;
                worker[_small] = 1;
                _worstDaysSet = false;
                _high = -1;
                _small = -1;
                break;
            }
        }
    }

    void runDayFunctions() {
        getDayErrors();
        getWorkerErrors();
        calculateDayOffDifferentials();
        findWorstDays();
    }


    void fixWorstWorkers() {
        // pitäs tutkailla niitä vitun viikonloppuja rip
    }

    boolean isSolved() {

        for (int i = 1; i <= 2; i++)
        {
            for (int debug : _calendar[_calendar.length - i]) {
                if (debug == 1) return false;
            }
        }
        //return true;

        // voiks tää jumalainen purkka ees toimia?!
/*
        for(int i = 0; i < get_breakpoints().size() - 1; i++) {
            if (!get_breakpoints().get(i).equals("[]")) return false;
        }
*/
        return true;
    }

    void ToString() {
        for (int[] _calendar1 : _calendar) {
            System.out.println(Arrays.toString(_calendar1));
        }
    }
}
