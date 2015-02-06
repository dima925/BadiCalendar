package de.pezeshki.bahaicalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
//import android.app.ActionBar;
//import android.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
//public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
//        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a SectionFragment (defined as a static inner class below).
            Fragment fragment = new SectionFragment();
            Bundle args = new Bundle();
            args.putInt(SectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A fragment representing a section of the app, that simply displays different tabs.
     */
    public static class SectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";
        String months1="", months2="", holydays1="", holydays2="";
        String badiDateString, gregorianDateString;
        View rootView;

        public void getDate() {
            // Get and display current Gregorian date
            // call Badi function and display results
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int doy = cal.get(Calendar.DAY_OF_YEAR);
            gregorianDateString = doyToGregorian(doy, year);
            calculateBadi(doy, year);

            Bundle args = getArguments();

            ((TextView) rootView.findViewById(R.id.gregrorian_date)).setText(gregorianDateString);
            ((TextView) rootView.findViewById(R.id.badi_date)).setText(badiDateString);

            if (args.getInt(ARG_SECTION_NUMBER)==1) {
                ((TextView) rootView.findViewById(R.id.upcoming)).setText(holydays1+holydays2);
            }else if (args.getInt(ARG_SECTION_NUMBER)==2) {
                ((TextView) rootView.findViewById(R.id.upcoming)).setText(months1+months2);
            }
        }
        @Override
        public void onResume() {
            super.onResume();
            // rerun when coming back from background
            getDate();
        }

        public void calculateBadi(int doy, int year) {
            // Calculate current Badi date and the upcoming holydays and feast
            // store in results in strings badiDateString, holydays1+holydays2, months1+months2
            int badiYear = 0;
            int dayOfBadiYear = 0;
            int yearIndex = year - 2014;
            int leapyear = isLeapYear(year);
            int nawRuz = nawRuzParameter(yearIndex);
            int nextYearIndex = yearIndex + 1;
            int nextYear = year + 1;
            int dayOffset = 78 + leapyear + nawRuz;
            int leapLastYear = isLeapYear(year-1);

            months1=getResources().getString(R.string.titleOut1)+"\n\n";
            holydays1=getResources().getString(R.string.titleOut2)+"\n\n";
            months2=""; holydays2="";

            int[] badiDate = badiDateFunction(doy, year);
            badiYear = badiDate[2];
            dayOfBadiYear = badiDate[3];
            badiDateString = bdoyToString(badiDate);
            if (badiDate[1] == 20) badiDateString +=  "\n" + getResources().getString(R.string.fast);

            if (doy < dayOffset){
                nextYearIndex = yearIndex;
                nextYear = year;
            }
            int nextBadiYear = badiYear + 1;
            int nawRuzNextYear = nawRuzParameter(nextYearIndex);
            int leapNextyear = isLeapYear(nextYear);
            int nawRuzLastYear = nawRuzParameter(yearIndex - 1);

            // List all Holy days
            for (int i = 0; i < 11; i++) {
                int hdday = holydays(i);
                // Birth of Bab and Baha'u'llah
                if (i == 7) {
                    hdday = twinBDays(yearIndex);
                } else if (i == 8) {
                    hdday = twinBDays(yearIndex) + 1;
                }
                // Holy day name from strings.xml (for multi language support)
                String hdName = hdName(i);
                String tmpString = "";
                int hdDoy;
                if (hdday == dayOfBadiYear) {
                    // Today is a holy day
                    badiDateString += "\n" + hdName + "\n";
                } else if (hdday > dayOfBadiYear) {
                    // The next Holy days
                    holydays1 += hdName + "\n" ;
                    hdDoy = bdoyToDoy1(hdday, leapyear, nawRuz);
                    int[] hdDate = badiDateFunction(hdDoy, year);
                    tmpString = bdoyToString(hdDate);
                    holydays1 += tmpString + "\n";
                    tmpString = doyToGregorian(hdDoy, year);
                    holydays1 += tmpString + "\n";
                    int diff=hdday-dayOfBadiYear;
                    if (diff<19){
                        if (diff>1) {
                            holydays1 += getResources().getString(R.string.in_prep) + " " + diff + " " + getResources().getString(R.string.days) + "\n\n";
                        }else{
                            holydays1 += getResources().getString(R.string.in_prep) + " " + diff + " " + getResources().getString(R.string.day) + "\n\n";
                        }
                    }else{
                        holydays1 += "\n";
                    }
                } else if (hdday < dayOfBadiYear) {
                    // The holy days next year
                    if (i == 7) {
                        hdday = twinBDays(nextYearIndex);
                    } else if (i == 8) {
                        hdday = twinBDays(nextYearIndex) + 1;
                    }
                    holydays2 += hdName + "\n";
                    hdDoy = bdoyToDoy2(hdday, leapNextyear, nawRuzNextYear);
                    int[] hdDate = badiDateFunction(hdDoy, nextYear);
                    tmpString = bdoyToString(hdDate);
                    holydays2 += tmpString + "\n";
                    tmpString = doyToGregorian(hdDoy, nextYear);
                    holydays2 += tmpString + "\n\n";
                }
            }

            // List the first days of each month
            for (int i = 0; i < 20; i++) {
                int mDay = 1 + i * 19, mDoy;
                String tmpString;
                // month of Ala is one day after 18 months and Ayyam'i'Ha (=18*19+4 or 5= 347)
                if (i==19) mDay = 347 + nawRuz*(1-nawRuzLastYear);
//            if (i==19) mDay = 347;
                if (mDay >= dayOfBadiYear) {
                    mDoy = bdoyToDoy1(mDay, leapyear, nawRuz);
                    if (i>15) mDoy = bdoyToDoy1(mDay, leapLastYear, nawRuzLastYear);
                    if (i==19) mDoy = bdoyToDoy1(mDay, leapyear, nawRuzLastYear);
                    int[] mDate = badiDateFunction(mDoy,year);
                    tmpString = bdoyToString(mDate);
                    months1 += tmpString + "\n";
                    tmpString = doyToGregorian(mDoy, year);
                    months1 += tmpString + "\n";
                    int diff=mDay-dayOfBadiYear;
                    if (diff<19){
                        if (diff>1) {
                            months1 += getResources().getString(R.string.in_prep) +" "+ diff  +" "+ getResources().getString(R.string.days)+"\n\n";
                        }else{
                            months1 += getResources().getString(R.string.in_prep) +" "+ diff  +" "+ getResources().getString(R.string.day)+"\n\n";
                        }
                    }else{
                        months1 += "\n";
                    }
                }else{
                    mDoy = bdoyToDoy2(mDay, leapNextyear, nawRuzNextYear);
                    if (i>15) mDoy = bdoyToDoy2(mDay, leapyear, nawRuz);
                    if (i==19) mDoy = bdoyToDoy2(mDay, leapNextyear, nawRuz);
                    int[] mDate = badiDateFunction(mDoy, year);
                    tmpString = bdoyToString(mDate);
                    months2 += tmpString + "\n";
                    tmpString = doyToGregorian(mDoy, nextYear);
                    months2 += tmpString + "\n\n";
                }
            }

            badiDateString += "\n";
        }

        public int bdoyToDoy1(int bdoy, int leapyear, int nawRuz) {
            // Returns the day of the Gregorian year; day is < 365 or 366
            // Input: Day of the Badi year, leapyear (1 yes, 0 no), and Nawruz on March 21 (1 yes, 0 no
            return (bdoy + 78 + leapyear + nawRuz)%(365+leapyear);
        }

        public int bdoyToDoy2(int bdoy, int leapyear, int nawRuz) {
            // Returns the day of the Gregorian year; day can be more than 365 or 366
            // Input: Day of the Badi year, leapyear (1 yes, 0 no), and Nawruz on March 21 (1 yes, 0 no
            return (bdoy + 78 + leapyear + nawRuz);
        }

        public int[] badiDateFunction(int doy,int year) {
            // Returns an array with the Badi Dates: Day of th month, month, year, day of the year
            // Input day of the Gregorian year and Gregorian year
            int badiYear = 0;
            int badiDay = 0, badiMonth = 0, dayOfBadiYear = 0;
            int yearIndex = year - 2014;
            int leapyear = isLeapYear(year);
            int nawRuz = nawRuzParameter(yearIndex);
            int nawRuzLastYear = nawRuzParameter(yearIndex - 1);

            // Calculate day number of the Badi Year
            if (doy < 78 + leapyear + nawRuz) {
                dayOfBadiYear = doy + 287 - nawRuzLastYear;
                badiYear = year - 1844;
            } else {
                dayOfBadiYear = doy - 78 - leapyear - nawRuz;
                badiYear = year - 1843;
            }

            // Calculate current Badi date
            badiDay=dayOfBadiYear%19;
            if(badiDay==0) badiDay=19;
            badiMonth = ((dayOfBadiYear - badiDay) / 19 + 1);
            // Month of Ala (19th month; after Ayyam'i'Ha)
            // Bug will occur in Ala 216!
            if ((dayOfBadiYear > (346 + nawRuz * (1-nawRuzLastYear)))) {
                badiMonth = 20;
                badiDay = dayOfBadiYear - 346 - nawRuz * (1-nawRuzLastYear);
            }

            int[] date = {badiDay,badiMonth,badiYear,dayOfBadiYear};
            return date;
        }

        public int isLeapYear(int year){
            // Is it a leap year?
            boolean isleapyear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
            return (isleapyear) ? 1 : 0;
        }

        public String doyToGregorian(int doy, int year) {
            // Return the date; input day of the year and year
            DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, doy);
            calendar.set(Calendar.YEAR, year);
            return dateFormat.format(calendar.getTime());
        }

        public String bdoyToString(int[] badiDate) {
            // Return the date; input badiDate (array day, month, doy) and year
            int bm;
            String badiString;
            String monthName;
            String[] monthArrabic = getResources().getStringArray(R.array.monthArabic);
            String[] monthTrans = getResources().getStringArray(R.array.month);

            bm = badiDate[1];
            monthName = monthArrabic[bm-1] + " - " + monthTrans[bm-1] + " (" + bm + ") ";
            if (bm == 20) monthName = monthArrabic[bm-1] + " - " + monthTrans[bm-1] + " (19) ";
            badiString = badiDate[0] + ". " + monthName + "." + badiDate[2];
            if (bm == 19) badiString = monthArrabic[bm-1] + " (" +monthTrans[bm-1]+ ") " + badiDate[2];
            return badiString;
        }

        public String hdName(int i){
            // Return the name of the Holy day
            String[] tmp;
            tmp=getResources().getStringArray(R.array.holyday);
            return tmp[i];
        }

        public int holydays(int i){
            // return the day of the year of a Holy day
            // the values for the Birth of Bab and Baha'u'llah are placeholder and are read from another array
            int[] hdArray = {1, 32, 40, 43, 65, 70, 112, 217, 237, 251, 253};
            return hdArray[i];
        }


        public int nawRuzParameter(int yearIndex) {
            // Naw Ruz on March 21st; list for the years 2014-2064
            int[] nrArray = {1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0};
            if (yearIndex<nrArray.length) {
                return nrArray[yearIndex];
            }else{
                return -1;
            }
        }


        public int twinBDays(int yearIndex) {
            // Return the Day of the year for the Birth of Bab; list for the years 2014-2064
            int[] nrArray = {236,238,227,216,234,223,213,232,220,210,228,217,235,224,214,233,223,211,230,219,238,226,215,234,224,213,232,221,210,228,217,236,225,214,233,223,212,230,219,237,227,215,234,224,213,232,220,209,228,218,236};
            if (yearIndex<nrArray.length) {
                return nrArray[yearIndex];
            }else{
                return -1;
            }
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            getDate();


            return rootView;
        }
    }
}