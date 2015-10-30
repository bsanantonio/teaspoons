package com.goodhousestudios.teaspoons;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class UnitConverterActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_unit_converter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Dry";
                case 1:
                    return "Fluid";
                case 2:
                    return "Weight";
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private EditText value;

        private Spinner startSpinner;
        private Spinner endSpinner;
        private ArrayAdapter<CharSequence> startAdapter;
        private ArrayAdapter<CharSequence> endAdapter;

        private TextView result;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_unit_converter, container, false);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    startSpinner = (Spinner) rootView.findViewById(R.id.start_spinner);
                    startAdapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.dry_liquid_array, android.R.layout.simple_spinner_item);
                    startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    startSpinner.setAdapter(startAdapter);

                    endSpinner = (Spinner) rootView.findViewById(R.id.end_spinner);
                    endAdapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.dry_liquid_array, android.R.layout.simple_spinner_item);
                    endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    endSpinner.setAdapter(endAdapter);
                    break;
                case 1:
                    startSpinner = (Spinner) rootView.findViewById(R.id.start_spinner);
                    startAdapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.dry_liquid_array, android.R.layout.simple_spinner_item);
                    startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    startSpinner.setAdapter(startAdapter);

                    endSpinner = (Spinner) rootView.findViewById(R.id.end_spinner);
                    endAdapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.liquid_array, android.R.layout.simple_spinner_item);
                    endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    endSpinner.setAdapter(endAdapter);
                    break;
                case 2:
                    startSpinner = (Spinner) rootView.findViewById(R.id.start_spinner);
                    startAdapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.weight_array, android.R.layout.simple_spinner_item);
                    startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    startSpinner.setAdapter(startAdapter);

                    endSpinner = (Spinner) rootView.findViewById(R.id.end_spinner);
                    endAdapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.weight_array, android.R.layout.simple_spinner_item);
                    endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    endSpinner.setAdapter(endAdapter);
                    break;
            }

            result = (TextView) rootView.findViewById(R.id.conversion_result);

            startSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    calculateConversion();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    calculateConversion();
                }
            });

            endSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    calculateConversion();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    calculateConversion();
                }
            });

            value = (EditText) rootView.findViewById(R.id.edit_measurement);
            value.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        calculateConversion();
                        handled = true;
                    }
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return handled;
                }
            });

            return rootView;
        }

        public void calculateConversion() {
            if (!value.getText().toString().isEmpty()) {
                double conversion = 0;
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setDecimalSeparatorAlwaysShown(false);
                switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                    case 0:
                        DryConverter startDryConverter = new DryConverter(startSpinner.getSelectedItem().toString());
                        conversion = startDryConverter.toCups(Double.parseDouble(value.getText().toString()));
                        DryConverter endDryConverter = new DryConverter(endSpinner.getSelectedItem().toString());
                        conversion = endDryConverter.fromCups(conversion);
                        break;
                    case 1:
                        LiquidConverter startLiquidConverter = new LiquidConverter(startSpinner.getSelectedItem().toString());
                        conversion = startLiquidConverter.toFluidOunces(Double.parseDouble(value.getText().toString()));
                        break;
                    case 2:
                        WeightConverter startWeightConverter = new WeightConverter(startSpinner.getSelectedItem().toString());
                        conversion = startWeightConverter.toOunces(Double.parseDouble(value.getText().toString()));
                        WeightConverter endWeightConverter = new WeightConverter(endSpinner.getSelectedItem().toString());
                        conversion = endWeightConverter.fromOunces(conversion);
                        break;
                }
                result.setText(decimalFormat.format(conversion));
            }
        }
    }
}
